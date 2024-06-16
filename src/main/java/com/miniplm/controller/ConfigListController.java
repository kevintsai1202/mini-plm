package com.miniplm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.repository.ConfigListItemRepository;
import com.miniplm.repository.ConfigListNodeRepository;
import com.miniplm.response.TableResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "下拉選單" , description = "與下拉選單相關的API")
@RestController
@RequestMapping({"/api/v1/admin/config/lists","/api/v1/config/lists"})
@CrossOrigin
public class ConfigListController {
	@Resource
	private ConfigListNodeRepository listNodeRepository;
	@Resource
	private ConfigListItemRepository listItemRepository;
	
	@GetMapping()
	@Operation(summary = "取得下拉選單列表",
		       description = "返回所有下拉選單列表")
	public ResponseEntity<TableResultResponse> list() {
		return ResponseEntity.ok(new TableResultResponse(listNodeRepository.findAll()));
	}
	
	@PostMapping()
	@Operation(summary = "建立新下拉選單",
               description = "創建新的下拉選單")
	public ResponseEntity<TableResultResponse> create(@RequestBody @Validated ConfigListNode listNode){
		return ResponseEntity.ok(new TableResultResponse(listNodeRepository.save(listNode)));
	}
	
	@GetMapping(value = "/{id}")
	@Operation(summary = "取得下拉選單內容",
               description = "依id返回下拉選單內容")
	public ResponseEntity<ConfigListNode> detail(@PathVariable("id") Long id) {
		Optional<ConfigListNode> temp = listNodeRepository.findById(id);
		ConfigListNode listNode=null;
		if (temp.isPresent()) {
			listNode = temp.get();
		}else {
			throw new EntityNotFoundException("no record found id="+id+" for ListNode");
		}
		return ResponseEntity.ok(listNode);
	}
	
	@PostMapping(value = "/{id}/listitems")
	@Operation(summary = "建立下拉選單選項",
               description = "在List Node=id下建立清單")
	public ResponseEntity<ConfigListItem> createListItems(@PathVariable("id") Long id, @RequestBody @Validated ConfigListItem listItem) {
		ConfigListNode listNode = listNodeRepository.getReferenceById(id);
		listItem.setListNode(listNode);
//		List<ConfigListItem> oldListItems = listNode.getListItems();
//		int orderBy = oldListItems.size();
//		for (ConfigListItem listItem : listItems) {
//			listItem.setListNode(listNode);
//			listItem.setOrderBy(++orderBy);
//		}
//		ConfigListItem savedListItems = listItemRepository.saveAll(listItems);
		return ResponseEntity.ok(listItemRepository.save(listItem));
	}
	
	@PutMapping(value = "/{id}")
	@Operation(summary = "更新下拉選單描述",
               description = "更新List Node資訊")
	public ResponseEntity<ConfigListNode> updateListNode(@PathVariable("id") Long id, @RequestBody @Validated ConfigListNode listNode) {
		ConfigListNode _listNode = listNodeRepository.getReferenceById(id);
		_listNode.setDescription(listNode.getDescription());
		_listNode.setName(listNode.getName());
//		_listNode.setEnabled(listNode.getEnabled());
//		for (int order = 0; order < listItems.size() ; order++) {
//			ConfigListItem newListItem = listItems.get(order);
//			ConfigListItem oldListItem = listItemRepository.getReferenceById(newListItem.getCliId());
//			oldListItem.setOrderBy(order+1);
//			listItemRepository.save(oldListItem);
//		}
//		List<ConfigListItem> updatedListItems = listItemRepository.saveAll(listItems);
		return ResponseEntity.ok(listNodeRepository.save(_listNode));
	}
	
	@GetMapping(value = "/{id}/listItems")
	@Operation(summary = "取得List id的所有list items",
               description = "取得List id的所有list items")
	public ResponseEntity<TableResultResponse> updateListItems(@PathVariable("id") Long id) {
		ConfigListNode listNode = listNodeRepository.getReferenceById(id);
//		List listItems = listNode.getListItems();
//		List<ConfigListItem> updatedListItems = listItemRepository.saveAll(listItems);
		return ResponseEntity.ok(new TableResultResponse(listNode.getListItems()));
	}
	
	@PutMapping(value = "/{nodeId}/listItems/{listItemId}")
	@Operation(summary = "更新下拉選單選項內容",
               description = "將List Node下的選項內容更新")
	public ResponseEntity<ConfigListItem> updateListItem(@PathVariable("nodeId") Long nodeId, @PathVariable("listItemId") Long listItemId,  @RequestBody @Validated ConfigListItem _listItem) {
		ConfigListNode listNode = listNodeRepository.getReferenceById(nodeId);
		ConfigListItem listItem = listItemRepository.getReferenceById(listItemId);
		listItem.setValue(_listItem.getValue());	
		listItem.setKey(_listItem.getKey());
//		List<ConfigListItem> updatedListItems = listItemRepository.saveAll(listItems);
		return ResponseEntity.ok(listItemRepository.save(listItem));
	}
	
	@PutMapping(value = "/order")
	@Operation(summary = "更新下拉選單排序",
               description = "將List Node下的選項排序更新")
	public ResponseEntity<List<ConfigListItem>> updateListItemsOrder(@RequestBody List<ConfigListItem> listItems) {
		
		List<ConfigListItem> updateListItems = new ArrayList<>(); 
		
		listItems.forEach(listItem->{
			ConfigListItem dbListItem = listItemRepository.getReferenceById(listItem.getCliId());
			dbListItem.setOrderBy(listItem.getOrderBy());
			updateListItems.add(dbListItem);
		});
		
		return ResponseEntity.ok(listItemRepository.saveAll(updateListItems));
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
