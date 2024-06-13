package com.miniplm.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigFormNumber;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigStepCriteria;
import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.exception.ConfigAlreadyUsedException;
import com.miniplm.repository.ConfigCriteriaNodeRepository;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.repository.ConfigStepCriteriaRepository;
import com.miniplm.repository.ConfigStepRepository;
import com.miniplm.repository.ConfigWorkflowRepository;
import com.miniplm.request.ConfigStepCriteriaRequest;
import com.miniplm.response.ConfigStepCriteriaResponse;
import com.miniplm.response.TableResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "流程設定" , description = "與流程相關的API")
@RestController
@RequestMapping({"/api/v1/admin/config/stepcriterias"})
@CrossOrigin
public class ConfigStepCriteriaController {

	@Resource
	private ConfigStepRepository configStepRepository;
	@Resource
	private ConfigStepCriteriaRepository configStepCriteriaRepository;
	@Resource
	private ConfigCriteriaNodeRepository configCriteriaNodeRepository;
	
	@GetMapping("/step/{stepId}")
	@Operation(summary = "依step id取得stepcriteria",
               description = "依step id取得stepcriteria")
	public ResponseEntity<TableResultResponse<ConfigStepCriteriaResponse>> getStepCriterias(@PathVariable("stepId") Long stepId) {
		ConfigStep cStep = configStepRepository.getReferenceById(stepId);
		
		List<ConfigStepCriteria> cscList=configStepCriteriaRepository.findBycStep(cStep);
		List<ConfigStepCriteriaResponse> cscResponseList = new LinkedList<>();
		
		for (ConfigStepCriteria csc:cscList) {
			cscResponseList.add(new ConfigStepCriteriaResponse(csc));
		}
		
		return ResponseEntity.ok(new TableResultResponse<ConfigStepCriteriaResponse>(cscResponseList));
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "刪除StepCriteria",
               description = "刪除StepCriteria")
	public ResponseEntity delete(@PathVariable("id") Long id){
		ConfigStepCriteria stepCriteria = configStepCriteriaRepository.getReferenceById(id);
		if (stepCriteria == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			configStepCriteriaRepository.delete(stepCriteria);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
	}
	
	@PutMapping("/{cscId}")
	@Operation(summary = "依id更新Step Criteria",
		       description = "透過id更新Step Criteria資料")
	public ResponseEntity<ConfigStepCriteria> updateStepCriteriaById(@PathVariable("cscId") Long cscId, @RequestBody ConfigStepCriteriaRequest cscRequest) {
		ConfigStepCriteria cscStep = configStepCriteriaRepository.getReferenceById(cscId);
		cscStep.setCCriteriaNode(configCriteriaNodeRepository.getReferenceById(cscRequest.getCnId()));
		
		cscStep.setOrderBy(cscRequest.getOrderBy());
		cscStep.setApprovers((cscRequest.getApprovers()!=null)?new LinkedList<>(Arrays.asList(cscRequest.getApprovers())):new LinkedList<>());
		cscStep.setNotifiers((cscRequest.getNotifiers()!=null)?new LinkedList<>(Arrays.asList(cscRequest.getNotifiers())):new LinkedList<>());
		cscStep.setRequiredFields((cscRequest.getRequiredFields() !=null)&&(cscRequest.getRequiredFields().size()>0)?new LinkedList<>(cscRequest.getRequiredFields()):new LinkedList<>());
		return ResponseEntity.ok(configStepCriteriaRepository.save(cscStep));
	}
//	
//	@GetMapping("/{id}/steps")
//	@Operation(summary = "取得流程關卡",
//               description = "依id返回流程關卡")
//	public ResponseEntity<TableResultResponse> steps(@PathVariable("id") Long id) {
//		ConfigWorkflow workflow = configWorkflowRepository.getReferenceById(id);
//		return ResponseEntity.ok(new TableResultResponse(workflow.getCSteps()));
//	}
	
	@PostMapping(value = "/step/{stepId}")
	@Operation(summary = "建立Step下的Criteria",
               description = "建立Step下的Criteria")
	public ResponseEntity<ConfigStepCriteria> createStepCriteria(@PathVariable("stepId") Long stepId, @RequestBody @Validated ConfigStepCriteriaRequest cscRequest) {
		System.out.println("ConfigStepCriteria: "+ cscRequest);
		ConfigStep step = configStepRepository.getReferenceById(stepId);
		ConfigCriteriaNode ccn = configCriteriaNodeRepository.getReferenceById(cscRequest.getCnId());
		LinkedList listApprovers = (cscRequest.getApprovers()!=null)?new LinkedList<>(Arrays.asList(cscRequest.getApprovers())):new LinkedList<>();
		LinkedList listNotifiers = (cscRequest.getNotifiers()!=null)?new LinkedList<>(Arrays.asList(cscRequest.getNotifiers())):new LinkedList<>();
		LinkedList requiredFields = (cscRequest.getRequiredFields()!=null)&&(cscRequest.getRequiredFields().size()>0)?new LinkedList<>(cscRequest.getRequiredFields()):new LinkedList<>();
		ConfigStepCriteria csc = ConfigStepCriteria.builder()
				.cStep(step)
				.cCriteriaNode(ccn)
				.approvers(listApprovers)
				.notifiers(listNotifiers)
				.requiredFields(requiredFields)
				.orderBy(cscRequest.getOrderBy())
				.build();
		return ResponseEntity.ok(configStepCriteriaRepository.save(csc));
	}
//	
//	@SneakyThrows
//	@DeleteMapping(value = "/{id}")
//	@Operation(summary = "刪除分類",
//               description = "依據id刪除分類")
//	public void delete(@PathVariable("id") Long id) {
//		categoryService.delete(id);
//	}
//	
//	@PostMapping(value = "/{caid}/contracts")
//	@Operation(summary = "建立合約範本",
//               description = "依據分類id創建該分類下的合約範本")
//	public ResponseEntity<ModelResponse> createTemplate(@PathVariable("caid") Long caid, @RequestBody @Validated ModelRequest contractRequest){
//		return ResponseEntity.ok(contractService.createTemplate(caid, contractRequest));
//	}
//	
//	@GetMapping(value = "/{caid}/contracts")
//	@Operation(summary = "取得分類下的合約範本列表",
//               description = "依據分類id取得該分類下所有合約範本列表")
//	public ResponseEntity<List<ModelResponse>> listTemplate(@PathVariable("caid") Long caid){
//		return ResponseEntity.ok(contractService.listCategoryModels(caid));
//	}
	
}
