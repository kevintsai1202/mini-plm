package com.miniplm.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
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

import com.miniplm.entity.ConfigCriteriaItem;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.LogicalEnum;
import com.miniplm.repository.ConfigCriteriaItemRepository;
import com.miniplm.repository.ConfigCriteriaNodeRepository;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.request.ConfigCriteriaNodeRequest;
import com.miniplm.response.TableResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Criteria" , description = "與Criteria相關的API")
@RestController
@RequestMapping({"/api/v1/admin/config/criterias","/api/v1/config/criterias"})
@CrossOrigin
public class ConfigCriteriaController {
	@Resource
	private ConfigCriteriaNodeRepository criteriaNodeRepository;
	@Resource
	private ConfigCriteriaItemRepository criteriaItemRepository;
	@Resource
	private ConfigFormTypeRepository configFormTypeRepository;
	
	
	@GetMapping()
	@Operation(summary = "取得Criteria列表",
		       description = "返回所有Criteria列表")
	public ResponseEntity<TableResultResponse<ConfigCriteriaNode>> list() {
		List<ConfigCriteriaNode> allCriteriaNodes =  criteriaNodeRepository.findAll();
		System.out.println("allCriteriaNodes:"+allCriteriaNodes);
		return ResponseEntity.ok(new TableResultResponse(allCriteriaNodes));
	}
	
	@GetMapping("/formType/{formTypeId}")
	@Operation(summary = "依Form Type Id取得Criteria列表",
		       description = "依Form Type Id返回所有Criteria列表")
	public ResponseEntity<TableResultResponse<ConfigCriteriaNode>> listByFormTypeId(@PathVariable("formTypeId") Long formTypeId) {
		ConfigFormType formType = configFormTypeRepository.getReferenceById(formTypeId);
		
//		List<ConfigCriteriaNode> allCriteriaNodes =  criteriaNodeRepository.findAll();
		
		List<ConfigCriteriaNode> formTypeCriteriaNodes =  criteriaNodeRepository.findByConfigFormType(formType);
		System.out.println("formTypeCriteriaNodes:"+formTypeCriteriaNodes);
		return ResponseEntity.ok(new TableResultResponse(formTypeCriteriaNodes));
	}
	
	@PostMapping()
	@Operation(summary = "建立新Criteria",
               description = "創建新的Criteria")
	public ResponseEntity<TableResultResponse> create(@RequestBody @Validated ConfigCriteriaNodeRequest criteriaNodeRequest){
		ConfigFormType formType = configFormTypeRepository.getReferenceById(criteriaNodeRequest.getCfId());
		
		ConfigCriteriaNode criteriaNode = ConfigCriteriaNode.builder()
				.criteriaName(criteriaNodeRequest.getCriteriaName())
				.configFormType(formType)
				.build();
		return ResponseEntity.ok(new TableResultResponse(criteriaNodeRepository.save(criteriaNode)));
	}
	
	@PostMapping("/{sourceId}/saveas")
	@Operation(summary = "複製Criteria",
               description = "複製Criteria")
	public ResponseEntity<TableResultResponse> saveAs(@PathVariable Long sourceId, @RequestBody @Validated ConfigCriteriaNodeRequest criteriaNodeRequest){
//		List<ConfigCriteriaItem> targetCriteriaItems = new ArrayList<>();
		ConfigCriteriaNode source = criteriaNodeRepository.getReferenceById(sourceId);
		List<ConfigCriteriaItem> sourceCriteriaItems = source.getCriteriaItems();
		
		ConfigFormType formType = configFormTypeRepository.getReferenceById(criteriaNodeRequest.getCfId());
		
		ConfigCriteriaNode criteriaNode = ConfigCriteriaNode.builder()
				.criteriaName(criteriaNodeRequest.getCriteriaName())
				.configFormType(formType)
				.build();
		
		ConfigCriteriaNode savedCriteriaNode = criteriaNodeRepository.save(criteriaNode);
		
		sourceCriteriaItems.forEach((ConfigCriteriaItem cItem)->{
			ConfigCriteriaItem target = new ConfigCriteriaItem();
			BeanUtils.copyProperties(cItem, target);
			target.setCiId(null);
			target.setCriteriaNode(savedCriteriaNode);
			criteriaItemRepository.save(target);
		});
		
		return ResponseEntity.ok(new TableResultResponse(savedCriteriaNode));
	}
	
	@GetMapping(value = "/{id}")
	@Operation(summary = "取得單一Criteria內容",
               description = "依id返回Criteria內容")
	public ResponseEntity<ConfigCriteriaNode> detail(@PathVariable("id") Long id) {
		Optional<ConfigCriteriaNode> temp = criteriaNodeRepository.findById(id);
		ConfigCriteriaNode criteriaNode=null;
		if (temp.isPresent()) {
			criteriaNode = temp.get();
		}else {
			throw new EntityNotFoundException("no record found id="+id+" for ListNode");
		}
		return ResponseEntity.ok(criteriaNode);
	}
	
	@PostMapping(value = "/{id}/criteriaitems")
	@Operation(summary = "建立下拉選單選項",
               description = "在Criteria Node=id下建立清單")
	public ResponseEntity<ConfigCriteriaItem> createCriteriaItems(@PathVariable("id") Long id, @RequestBody @Validated ConfigCriteriaItem criteriaItem) {
		ConfigCriteriaNode criteriaNode = criteriaNodeRepository.getReferenceById(id);
		criteriaItem.setCriteriaNode(criteriaNode);
		if (criteriaItem.getLogical() == null) 
			criteriaItem.setLogical(LogicalEnum.AND);
		return ResponseEntity.ok(criteriaItemRepository.save(criteriaItem));
	}
	
	@PutMapping(value = "/{id}")
	@Operation(summary = "更新Criteria資料",
               description = "更新Criteria資料")
	public ResponseEntity<ConfigCriteriaNode> updateCriteriaNode(@PathVariable("id") Long id, @RequestBody @Validated ConfigCriteriaNodeRequest criteriaNodeReq) {
		ConfigCriteriaNode _criteriaNode = criteriaNodeRepository.getReferenceById(id);
		System.out.println("criteriaNodeReq: "+ criteriaNodeReq);
		ConfigFormType cFormType = null;
		_criteriaNode.setCriteriaName(criteriaNodeReq.getCriteriaName());
		
		Long cFormTypeId = criteriaNodeReq.getCfId();
		if (cFormTypeId != null) {
			cFormType = configFormTypeRepository.getReferenceById(cFormTypeId);
			_criteriaNode.setConfigFormType(cFormType);
		}
		return ResponseEntity.ok(criteriaNodeRepository.save(_criteriaNode));
	}
	
	@GetMapping(value = "/{id}/criteriaitems")
	@Operation(summary = "取得 Criteria Node id 下的所有Criteria items",
               description = "取得Criteria Node id的所有Criteria items")
	public ResponseEntity<TableResultResponse> getCriteriaItems(@PathVariable("id") Long id) {
		ConfigCriteriaNode criteriaNode = criteriaNodeRepository.getReferenceById(id);
		return ResponseEntity.ok(new TableResultResponse(criteriaNode.getCriteriaItems()));
	}
	
	@PutMapping(value = "/criteriaitems/{criteriaItemId}")
	@Operation(summary = "更新Criteria item內容",
               description = "將Criteria item內容更新")
	public ResponseEntity<ConfigCriteriaItem> updateCriteriaItem(@PathVariable("criteriaItemId") Long criteriaItemId,  @RequestBody @Validated ConfigCriteriaItem _criteriaItem) {
//		ConfigCriteriatNode criteriaNode = criteriaNodeRepository.getReferenceById(nodeId);
		ConfigCriteriaItem criteriaItem = criteriaItemRepository.getReferenceById(criteriaItemId);
		criteriaItem.setValue(_criteriaItem.getValue());	
		criteriaItem.setField(_criteriaItem.getField());
		criteriaItem.setLogical(_criteriaItem.getLogical());
		criteriaItem.setOperator(_criteriaItem.getOperator());
//		criteriaItem.setOrderBy(_criteriaItem.getOrderBy());
//		List<ConfigListItem> updatedListItems = listItemRepository.saveAll(listItems);
		return ResponseEntity.ok(criteriaItemRepository.save(criteriaItem));
	}
	
	@DeleteMapping(value = "/criteriaitems/{criteriaItemId}")
	@Operation(summary = "刪除Criteria item",
               description = "將Criteria item刪除")
	public ResponseEntity deleteCriteriaItem(@PathVariable("criteriaItemId") Long criteriaItemId) {
//		ConfigCriteriatNode criteriaNode = criteriaNodeRepository.getReferenceById(nodeId);
		ConfigCriteriaItem criteriaItem = criteriaItemRepository.getReferenceById(criteriaItemId);
//		List<ConfigListItem> updatedListItems = listItemRepository.saveAll(listItems);
		criteriaItemRepository.delete(criteriaItem);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping(value = "/{nodeId}/criteriaItems/order")
	@Operation(summary = "更新Criteria items排序",
               description = "將Criteria Node下的選項排序更新")
	public ResponseEntity<TableResultResponse> updateCriteriaItemsOrder(@PathVariable("nodeId") Long nodeId, @RequestBody @Validated List<ConfigCriteriaItem> criteriaItems) {
		ConfigCriteriaNode criteriaNode = criteriaNodeRepository.getReferenceById(nodeId);
		for (int order = 0; order < criteriaItems.size() ; order++) {
			ConfigCriteriaItem newCriteriaItem = criteriaItems.get(order);
			ConfigCriteriaItem dbCriteriaItem = criteriaItemRepository.getReferenceById(newCriteriaItem.getCiId());
//			dbCriteriaItem.setOrderBy(order+1);
			criteriaItemRepository.save(dbCriteriaItem);
		}
//		List<ConfigListItem> updatedListItems = listItemRepository.saveAll(listItems);
		return ResponseEntity.ok(new TableResultResponse(criteriaNode.getCriteriaItems()));
	}
	
}
