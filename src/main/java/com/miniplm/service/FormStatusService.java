package com.miniplm.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.miniplm.entity.ChangeTypeEnum;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.Form;
import com.miniplm.entity.FormHistory;
import com.miniplm.entity.ZAccount;
import com.miniplm.exception.BusinessException;
import com.miniplm.repository.ActionRepository;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.repository.ConfigWorkflowRepository;
import com.miniplm.repository.FormDataRepository;
import com.miniplm.repository.FormHistoryRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.repository.UserRepository;
import com.miniplm.response.WorkflowResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


//用來產生流程數據的Service
@Service
@RequiredArgsConstructor
@Slf4j
public class FormStatusService {
	@Resource
	private FormRepository formRepository;
	
	@Resource
	private FormDataRepository formDataRepository;
	
	@Resource
	private UserRepository userRepository;
	
	@Resource
	private ConfigFormTypeRepository configFormTypeRepository;
	
	@Resource
	private ConfigWorkflowRepository configWorkflowRepository;
	
	@Resource
	private ConfigFormNumberRepository configFormNumberRepository;
	
	@Resource
	private FormHistoryRepository formHistoryRepository;
	
	@Resource
	private ActionRepository actionRepository;
	
	@Autowired
	private ActionService actionService;
	
	@Transactional
	public WorkflowResponse getFormWorkflow(Long formId) {
		WorkflowResponse workflow = new WorkflowResponse();
		Form form = formRepository.getReferenceById(formId);
		List<ConfigStep> steps = form.getCWorkflow().getCSteps();
		log.info("form.getCurrStep(): {}",form.getCurrStep());
//		System.out.println("form.getCurrStep():"+form.getCurrStep());
		for (int i = 0; i < steps.size() ; i++) {
			log.info("steps.get(i): {}", steps.get(i));
//			System.out.println("steps.get(i)"+steps.get(i));
			if (form.getCurrStep().getStepName().equals(steps.get(i).getStepName())) {
				workflow.setCurrent(i);
				break;
			}
		}
		workflow.setSteps(steps);
		return workflow;
	}
	
//	@Transactional
//	public ConfigStep changeToFirstStep(Long formId) {
//		Form form = formRepository.getReferenceById(formId);
//		ConfigStep currStep = form.getCurrStep();
//		List<ConfigStep> steps = form.getCWorkflow().getCSteps();
//		System.out.println("form.getCurrStep():"+form.getCurrStep());
//		System.out.println("steps.get(0):"+steps.get(0));
//		if (steps.get(0) == currStep) {
//			form.setCurrStep(currStep.getNextStep());
//			Form savedForm = formRepository.save(form);
//			return savedForm.getCurrStep();
//		}else {
//			throw new BusinessException("Form is not Pending!");
//		}
//	}
	
	@Transactional
	public ConfigStep changeToNextStep(Long formId) {
		Form form = formRepository.getReferenceById(formId);
		
//		actionService.initNextStepActions(form);
		
		ConfigStep currStep = form.getCurrStep();
		ConfigStep nextStep = currStep.getNextStep();
		if (nextStep != null) {
			log.info("auto Change to:{}", nextStep.getStepName());
			ConfigStep newStep = autoChangeStep(form, nextStep);
			
			actionService.ignoreStepActions(formId, currStep.getCsId());
			form.setCurrStep(newStep);
			Form savedForm = formRepository.save(form);
			
			actionRepository.updateCurrCanNoticeActions(formId, newStep.getCsId());
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    	ZAccount user = null;
			if (authentication != null && authentication.isAuthenticated()) {
				user = (ZAccount) authentication.getPrincipal();
			}
			
			FormHistory history = FormHistory.builder()
	    			.detail("From: "+ currStep.getStepName()+ " To: "+ newStep.getStepName())
	    			.form(form)
	    			.accountName(user.getUsername())
	    			.stepName(currStep.getStepName())
	    			.type(ChangeTypeEnum.ChangeStatus)
	    			.build();
	    	formHistoryRepository.save(history);
			
			return newStep;
		}
		return currStep;
		
//		List<ConfigStep> steps = form.getCWorkflow().getCSteps();
//		System.out.println("form.getCurrStep():"+form.getCurrStep());
//		System.out.println("steps.get(0):"+steps.get(0));
//		if (steps.get(0) == currStep) {
//			form.setCurrStep(currStep.getNextStep());
//			Form savedForm = formRepository.save(form);
//			return savedForm.getCurrStep();
//		}else {
//			throw new BusinessException("Form is not Pending!");
//		}
	}
	
	@Transactional
	public ConfigStep autoChangeStep(Form form, ConfigStep newStep){
		Long cstepId = newStep.getCsId();
		ConfigStep tempStep = newStep;
		actionService.initStepActions(form, tempStep);
		int countByApprovers = actionRepository.countApproversByFormIdAndStepId(form.getFId(), cstepId);
		log.info("new step name: {}" , newStep.getStepName());
//		System.out.println("new step name:" + newStep.getStepName());
		log.info("countByApprovers: {}", countByApprovers);
//		System.out.println("countByApprovers:" + countByApprovers);
//		do {
		while ((countByApprovers == 0) && (tempStep.getNextStep() != null)) {
//		System.out.println("countByApprovers:"+countByApprovers);
//		form.setCurrStep(currStep.getNextStep());
			tempStep = tempStep.getNextStep();
			actionService.initStepActions(form, tempStep);
			countByApprovers = actionRepository.countApproversByFormIdAndStepId(form.getFId(), tempStep.getCsId());
//		System.out.println("tempStep:"+tempStep.getStepName());
			
			
			log.info("temp step Mame: {}" , tempStep.getStepName());
//			System.out.println("temp step Mame:" + tempStep.getStepName());
			
			log.info("countByApprovers: {}" + countByApprovers);
//			System.out.println("countByApprovers:" + countByApprovers);
			// Change Status
			// System.out.println("Need Change Status");
		}
		log.info("Step Change to: {}" , tempStep.getStepName());
//		System.out.println("Step Change to:" + tempStep.getStepName());
		form.setCurrStep(tempStep);
		form = formRepository.save(form);
		actionRepository.updateCurrCanNoticeActions(form.getFId(), tempStep.getCsId());
		return tempStep;
	}
}
