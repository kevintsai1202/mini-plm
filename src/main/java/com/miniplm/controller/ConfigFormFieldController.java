package com.miniplm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.entity.ListTypeEnum;
import com.miniplm.repository.ConfigFormFieldRepository;
import com.miniplm.repository.ConfigListNodeRepository;
import com.miniplm.request.ConfigFormFieldRequest;
import com.miniplm.response.TableResultResponse;
import com.miniplm.response.UserFormFieldResponse;
import com.miniplm.service.ConfigFormFieldService;
import com.miniplm.service.QueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "ConfigFormField", description = "與Config Form Field相關的API")
@RestController
@RequestMapping({ "/api/v1/admin/config/formfields", "/api/v1/config/formfields" })
@CrossOrigin
@Slf4j
public class ConfigFormFieldController {
	@Resource
	ConfigFormFieldRepository configFormFieldRepository;

	@Resource
	ConfigListNodeRepository configListNodeRepository;

	@Autowired
	ConfigFormFieldService configFormFieldService;

	@Autowired
	QueryService queryService;

	@GetMapping("/{id}")
	public ResponseEntity<UserFormFieldResponse> getById(@PathVariable("id") Long id) {
		ConfigFormField cff = configFormFieldRepository.getReferenceById(id);
		ConfigListNode cln = cff.getConfigListNode();
		List<ConfigListItem> cListItems = new ArrayList<>();
		if (cln != null) {
			if (cln.getListType().equals(ListTypeEnum.sql) && cln.getSql() != null) {
				cListItems = queryService.getListItemsWithNativeQuery(cln.getSql());
			} else {
				cListItems = cln.getListItems();
			}
		}
		return ResponseEntity.ok(new UserFormFieldResponse(cff, cListItems, false));
	}

	@PutMapping("/{id}/switchvisible")
	public ResponseEntity<UserFormFieldResponse> switchVisible(@PathVariable("id") Long id) {
		log.info("Switch Visible id: {}", id);
//		System.out.println("switch Visible id:"+ id);
		ConfigFormField cff = configFormFieldService.switchVisible(id);
		ConfigListNode cln = cff.getConfigListNode();
		List<ConfigListItem> cListItems = new ArrayList<>();
		if (cln != null) {
			if (cln.getListType().equals(ListTypeEnum.sql) && cln.getSql() != null) {
				cListItems = queryService.getListItemsWithNativeQuery(cln.getSql());
			} else {
				cListItems = cln.getListItems();
			}
		}

		return ResponseEntity.ok(new UserFormFieldResponse(cff, cListItems, false));
	}

	@PutMapping("/{id}/switchrequired")
	public ResponseEntity<UserFormFieldResponse> switchRequired(@PathVariable("id") Long id) {
		log.info("Switch Required id: {}", id);
//		System.out.println("switch Required id:"+ id);
		ConfigFormField cff = configFormFieldService.switchRequired(id);
		ConfigListNode cln = cff.getConfigListNode();
		List<ConfigListItem> cListItems = new ArrayList<>();
		if (cln != null) {
			if (cln.getListType().equals(ListTypeEnum.sql) && cln.getSql() != null) {
				cListItems = queryService.getListItemsWithNativeQuery(cln.getSql());
			} else {
				cListItems = cln.getListItems();
			}
		}

		return ResponseEntity.ok(new UserFormFieldResponse(cff, cListItems, false));
	}

//	@GetMapping("/{id}/switchmultiple")
//	public ResponseEntity<UserFormFieldResponse> switchMultiple(@PathVariable("id") Long id) {
//		System.out.println("switch multiple id:"+ id);
//		ConfigFormField cff = configFormFieldService.switchMultiple(id);
//		return ResponseEntity.ok(new UserFormFieldResponse(cff));
//	}

	@PutMapping("/{id}")
	@Operation(summary = "依id更新Field設定", description = "依id更新Field設定")
	public ResponseEntity<ConfigFormField> updateFormDataById(@PathVariable("id") Long id,
			@RequestBody ConfigFormFieldRequest fieldReq) {
		ConfigFormField oldField = configFormFieldRepository.getReferenceById(id);
		if (oldField != null) {
			Long clnId = fieldReq.getClnId();
			if (clnId != null) {
				ConfigListNode cln = configListNodeRepository.getReferenceById(clnId);
				oldField.setConfigListNode(cln);
			}
			oldField.setFieldName(fieldReq.getFieldName());
			oldField.setGroups(fieldReq.getGroups());
			oldField.setOrderBy(fieldReq.getOrderBy());
			oldField.setPattern(fieldReq.getPattern());
			oldField.setPatternMsg(fieldReq.getPatternMsg());
			return ResponseEntity.ok(configFormFieldRepository.save(oldField));
		} else
			throw new EntityNotFoundException();
	}

	@PutMapping("/order")
	@Operation(summary = "修改field的順序", description = "修改field的順序")
	public ResponseEntity<List<ConfigFormField>> updateFieldsOrder(
			@RequestBody List<ConfigFormFieldRequest> fieldReqs) {

		List<ConfigFormField> updateFields = new ArrayList<>();

		fieldReqs.forEach(field -> {
			ConfigFormField dbField = configFormFieldRepository.getReferenceById(field.getCffId());
			dbField.setOrderBy(field.getOrderBy());
			updateFields.add(dbField);
		});

		return ResponseEntity.ok(configFormFieldRepository.saveAll(updateFields));
	}

	@GetMapping("/formtype/{id}")
	public ResponseEntity<TableResultResponse> getFormTypeFields(@PathVariable("id") Long id) {
		log.info("id: {}", id);
//		System.out.println("id:"+id.toString());
		List<ConfigFormField> fields = configFormFieldRepository.findByFormType(id);
		return ResponseEntity.ok(new TableResultResponse<ConfigFormField>(fields));
	}

	@GetMapping("/formtype/{id}/groups")
	public ResponseEntity<Map<String, String>> getFieldGroups(@PathVariable("id") Long formTypeId) {
		log.info("id: {}", formTypeId);
//		System.out.println("id:"+id.toString());
		return ResponseEntity.ok(configFormFieldService.getGroupList(formTypeId));
	}

}
