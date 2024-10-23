package com.miniplm.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.miniplm.convert.ConfigWorkflowConverter;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigStepCriteria;
import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.entity.Form;
import com.miniplm.exception.BusinessException;
import com.miniplm.repository.ConfigCriteriaNodeRepository;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.repository.ConfigStepCriteriaRepository;
import com.miniplm.repository.ConfigStepRepository;
import com.miniplm.repository.ConfigWorkflowRepository;
import com.miniplm.request.ConfigWorkflowRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfigWorkflowService {
	@Resource
	ConfigWorkflowRepository configWorkflowRepository;
	@Resource
	ConfigFormTypeRepository configFormTypeRepository;
	@Resource
	ConfigStepRepository configStepRepository;
	@Resource
	ConfigStepCriteriaRepository configStepCriteriaRepository;
	@Resource
	ConfigCriteriaNodeRepository configCriteriaNodeRepository;
	
	
	@Transactional
	public ConfigWorkflow createWorkflow(ConfigWorkflowRequest cwfReq) {
		ConfigWorkflow cWorkflow = ConfigWorkflowConverter.INSTANCT.requestToEntity(cwfReq);
		cWorkflow = configWorkflowRepository.save(cWorkflow);
		ConfigFormType formType = configFormTypeRepository.getReferenceById(cwfReq.getCfId());
		formType.setConfigWorkflow(cWorkflow);
		return cWorkflow;
	}
	
	@Transactional
	public ConfigWorkflow updateWorkflow(Long cwfId, ConfigWorkflowRequest cwfReq) {
		ConfigWorkflow dbWorkflow = configWorkflowRepository.getReferenceById(cwfId);
		dbWorkflow.setDescription(cwfReq.getDescription());
		dbWorkflow.setName(cwfReq.getName());
		dbWorkflow.setStatus(cwfReq.getStatus());
		dbWorkflow = configWorkflowRepository.save(dbWorkflow);
		ConfigFormType configFormType = configFormTypeRepository.getReferenceById(cwfReq.getCfId());
		configFormType.setConfigWorkflow(dbWorkflow);
		configFormTypeRepository.save(configFormType);
		return dbWorkflow;
	}
	
	@Transactional
	public ConfigStep createWorkflowStep(Long workflowId, ConfigStep cStep) {
		ConfigStep newConfigStep = null;
		ConfigWorkflow cWorkflow = null;
		cWorkflow = configWorkflowRepository.getReferenceById(workflowId);
		if (cWorkflow.getEnabled()) {
			throw new BusinessException("Workflow has enabled, can't update Steps");
		}
		cStep.setCWorkflow(cWorkflow);
		newConfigStep = configStepRepository.save(cStep);
//		configWorkflowRepository.save(cWorkflow);
		return newConfigStep;
	}
	
	@Transactional
	public ConfigWorkflow switchStatusWorkflow(Long workflowId) {
		ConfigWorkflow workflow = configWorkflowRepository.getReferenceById(workflowId);
		initSteps(workflow.getCSteps());
		log.info("workflow status: {}" ,workflow.getStatus());
		workflow.setStatus(!workflow.getStatus());
		return configWorkflowRepository.save(workflow);
	}
	
	@Transactional
	public ConfigStepCriteria addStepCriteria(Long stepId, Long criteriaNodeId) {
		ConfigStepCriteria cStepCriteria = null;
		ConfigStep step = null;
		ConfigCriteriaNode cCriteriaNode = null;
		
		cCriteriaNode = configCriteriaNodeRepository.getReferenceById(criteriaNodeId);
		step = configStepRepository.getReferenceById(stepId);
		
		cStepCriteria = ConfigStepCriteria.builder()
				.cStep(step)
				.cCriteriaNode(cCriteriaNode)
				.build();
		
		cStepCriteria = configStepCriteriaRepository.save(cStepCriteria);
		return cStepCriteria;
	}
	
	@Transactional
	public void initSteps(List<ConfigStep> steps) {
		for (int i = 0 ; i < steps.size() ; i++) {
			ConfigStep step = steps.get(i);
			if (i < steps.size()-1) {
				step.setNextStep(steps.get(i+1));
				log.info("{} :Curr step: {} set next step: {}", i, step.getStepName(), steps.get(i+1).getStepName());
//				System.out.println(i+":Curr step:"+step.getStepName()+" set next step:"+steps.get(i+1).getStepName());
				configStepRepository.save(step);
			}
		}
	}
	
	public static ConfigStep getFirstStep(Form form) {
		return form.getCWorkflow().getCSteps().get(0);
	}
}
