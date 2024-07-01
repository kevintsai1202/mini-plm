package com.miniplm.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

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
import com.miniplm.response.TableResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "流程設定" , description = "與流程相關的API")
@RestController
@RequestMapping({"/api/v1/admin/config/steps","/api/v1/config/steps"})
@CrossOrigin
@Slf4j
public class ConfigStepController {

	@Resource
	private ConfigStepRepository configStepRepository;
	
	@Transactional
	@GetMapping("/{id}")
	@Operation(summary = "取得關卡內容",
               description = "依id返回關卡設定內容")
	public ResponseEntity<ConfigStep> detail(@PathVariable("id") Long id) {
		return ResponseEntity.ok(configStepRepository.getReferenceById(id));
	}
	
	@Transactional
	@DeleteMapping("/{id}")
	@Operation(summary = "刪除關卡",
               description = "刪除關卡")
	public ResponseEntity<Long> delete(@PathVariable("id") Long id){
		ConfigStep step = configStepRepository.getReferenceById(id);
		if (step == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			configStepRepository.delete(step);
			return new ResponseEntity<>(id, HttpStatus.OK);
		}
		
	}
	
	@Transactional
	@PutMapping("/{id}")
	@Operation(summary = "依id更新關卡",
		       description = "透過id更新關卡資料")
	public ResponseEntity<ConfigStep> updateConfigStepById(@PathVariable("id") Long id, @RequestBody ConfigStepRequest stepRequest) {
		log.info("stepRequest: {}", stepRequest);
//		ConfigStep step = ConfigStepConverter.INSTANCT.requestToEntity(stepRequest);
		ConfigStep rejectStep = (stepRequest.getRejectStepId() == null)?null:configStepRepository.getReferenceById(stepRequest.getRejectStepId());
		ConfigStep dbStep = configStepRepository.getReferenceById(id);
		log.info("dbStep: {}", dbStep);
		dbStep.setDescription(stepRequest.getDescription());
		dbStep.setStepName(stepRequest.getStepName());
		dbStep.setOrderBy(stepRequest.getOrderBy());
		dbStep.setApprovers(stepRequest.getApprovers());
		dbStep.setNotifiers(stepRequest.getNotifiers());
		dbStep.setRejectStep(rejectStep);
		return ResponseEntity.ok(configStepRepository.save(dbStep));
	}
//	
//	@GetMapping("/{id}/steps")
//	@Operation(summary = "取得流程關卡",
//               description = "依id返回流程關卡")
//	public ResponseEntity<TableResultResponse> steps(@PathVariable("id") Long id) {
//		ConfigWorkflow workflow = configWorkflowRepository.getReferenceById(id);
//		return ResponseEntity.ok(new TableResultResponse(workflow.getCSteps()));
//	}
	
//	@PostMapping(value = "/{id}/steps")
//	@Operation(summary = "建立流程新關卡",
//               description = "在Workflow=id下建立關卡")
//	public ResponseEntity<ConfigStep> createStep(@PathVariable("id") Long id, @RequestBody @Validated ConfigStep step) {
//		ConfigWorkflow workflow = configWorkflowRepository.getReferenceById(id);
//		step.setCWorkflow(workflow);
//		List<ConfigListItem> oldListItems = listNode.getListItems();
//		int orderBy = oldListItems.size();
//		for (ConfigListItem listItem : listItems) {
//			listItem.setListNode(listNode);
//			listItem.setOrderBy(++orderBy);
//		}
//		ConfigListItem savedListItems = listItemRepository.saveAll(listItems);
//		return ResponseEntity.ok(configStepRepository.save(step));
//	}
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
