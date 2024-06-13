package com.miniplm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
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

import com.miniplm.convert.ConfigStepConverter;
import com.miniplm.entity.ConfigFormNumber;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.exception.ConfigAlreadyUsedException;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.repository.ConfigStepRepository;
import com.miniplm.repository.ConfigWorkflowRepository;
import com.miniplm.request.ConfigStepRequest;
import com.miniplm.response.ConfigStepResponse;
import com.miniplm.response.ConfigWorkflowResponse;
import com.miniplm.response.TableResultResponse;
import com.miniplm.service.ConfigWorkflowService;
import com.miniplm.service.FormDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "流程設定" , description = "與流程相關的API")
@RestController
@RequestMapping({"/api/v1/admin/config/workflows", "/api/v1/config/workflows"})
@CrossOrigin
@Slf4j
public class ConfigWorkflowController {

	@Resource
	private ConfigWorkflowRepository configWorkflowRepository;
	@Resource
	private ConfigStepRepository configStepRepository;
	
	@Autowired
	private ConfigWorkflowService configWorkflowService;
	
	@GetMapping()
	@Operation(summary = "取得表單編號列表",
		       description = "返回所有表單編號設定")
	public ResponseEntity<TableResultResponse<ConfigWorkflowResponse>> list() {
		List<ConfigWorkflow> workflows = configWorkflowRepository.findAll();
//		List<ConfigFormNumberResponse> formNumbers = configFormNumberRepository.quickListAll();
		List<ConfigWorkflowResponse> listWorkflowResponse = new ArrayList<>();
		for (ConfigWorkflow workflow : workflows) {
			listWorkflowResponse.add(new ConfigWorkflowResponse(workflow));
		}
		return ResponseEntity.ok(new TableResultResponse(listWorkflowResponse));
	}
	
	@GetMapping("/openworkflows")
	@Operation(summary = "取得已開啟表單編號列表",
		       description = "返回已開啟表單編號")
	public ResponseEntity<TableResultResponse<ConfigWorkflow>> listOpenWorkflows() {
		List<ConfigWorkflow> workflows = configWorkflowRepository.listOpenWorkflow();
//		List<ConfigFormNumberResponse> formNumbers = configFormNumberRepository.quickListAll();
		return ResponseEntity.ok(new TableResultResponse(workflows));
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "取得表單編號設定內容",
               description = "依id返回表單編號設定內容")
	public ResponseEntity<ConfigWorkflow> detail(@PathVariable("id") Long id) {
		return ResponseEntity.ok(configWorkflowRepository.getReferenceById(id));
	}
	
	@PostMapping()
	@Operation(summary = "建立新流程",
               description = "創建新的流程")
	public ResponseEntity<ConfigWorkflow> create(@RequestBody @Validated ConfigWorkflow workflow){
		return ResponseEntity.ok(configWorkflowRepository.save(workflow));
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "刪除流程",
               description = "刪除流程設定")
	public ResponseEntity<Long> delete(@PathVariable("id") Long id){
		ConfigWorkflow workflow = configWorkflowRepository.getReferenceById(id);
		if (workflow == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			configWorkflowRepository.delete(workflow);
			return new ResponseEntity<>(id, HttpStatus.OK);
		}
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "依id更新流程",
		       description = "透過id更新流程的資料")
	public ResponseEntity<ConfigWorkflow> updateWorkflowById(@PathVariable("id") Long id, @RequestBody ConfigWorkflow workflow) {
		ConfigWorkflow oldWorkflow = configWorkflowRepository.getReferenceById(id);
		oldWorkflow.setDescription(workflow.getDescription());
		oldWorkflow.setName(workflow.getName());
		oldWorkflow.setStatus(workflow.getStatus());
		return ResponseEntity.ok(configWorkflowRepository.save(oldWorkflow));
	}
	
	@PutMapping("/{id}/switchWorkflowStatus")
	@Operation(summary = "依id更新流程",
		       description = "透過id更新流程的資料")
	public ResponseEntity<ConfigWorkflow> switchStatusWorkflow(@PathVariable("id") Long id) {
		return ResponseEntity.ok(configWorkflowService.switchStatusWorkflow(id));
	}
	
	@GetMapping("/{id}/steps")
	@Operation(summary = "取得流程關卡",
               description = "依id返回流程關卡")
	public ResponseEntity<TableResultResponse> steps(@PathVariable("id") Long id) {
		ConfigWorkflow workflow = configWorkflowRepository.getReferenceById(id);
		List<ConfigStepResponse> stepResponseList = ConfigStepConverter.INSTANCT.entityToResponse(workflow.getCSteps());
		
		return ResponseEntity.ok(new TableResultResponse(stepResponseList));
	}
	
	@PostMapping(value = "/{id}/steps")
	@Operation(summary = "建立流程新關卡",
               description = "在Workflow=id下建立關卡")
	public ResponseEntity<ConfigStepResponse> createStep(@PathVariable("id") Long id, @RequestBody @Validated ConfigStepRequest stepRequest) {
		ConfigWorkflow workflow = configWorkflowRepository.getReferenceById(id);
		ConfigStep step = ConfigStepConverter.INSTANCT.requestToEntity(stepRequest);
		
		ConfigStep rejectStep = (stepRequest.getRejectStepId() == null)?null:configStepRepository.getReferenceById(stepRequest.getRejectStepId());
		step.setCWorkflow(workflow);
		step.setRejectStep(rejectStep);
//		List<ConfigListItem> oldListItems = listNode.getListItems();
//		int orderBy = oldListItems.size();
//		for (ConfigListItem listItem : listItems) {
//			listItem.setListNode(listNode);
//			listItem.setOrderBy(++orderBy);
//		}
//		ConfigListItem savedListItems = listItemRepository.saveAll(listItems);
		return ResponseEntity.ok(ConfigStepConverter.INSTANCT.entityToResponse(configStepRepository.save(step)));
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
