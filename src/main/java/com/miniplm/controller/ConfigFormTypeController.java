package com.miniplm.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.miniplm.entity.ConfigFormNumber;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigTableHeader;
import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.repository.ConfigTableHeaderRepository;
import com.miniplm.repository.ConfigWorkflowRepository;
import com.miniplm.request.ConfigFormTypeRequest;
import com.miniplm.response.ConfigFormFieldEnum;
import com.miniplm.response.TableResultResponse;
import com.miniplm.response.UserFormFieldResponse;
import com.miniplm.service.ConfigFormFieldService;
import com.miniplm.service.ConfigFormTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "ConfigFormType" , description = "與Config Form Type相關的API")
@RestController
@RequestMapping({"/api/v1/admin/config/formtypies", "/api/v1/config/formtypies"})
@CrossOrigin
@Slf4j
public class ConfigFormTypeController {
	@Resource
	ConfigFormTypeRepository configFormTypeRepository;
	
	@Resource
	ConfigFormNumberRepository configFormNumberRepository;
	
	@Resource
	ConfigWorkflowRepository configWorkflowRepository;
	
	@Resource
	ConfigTableHeaderRepository configTableHeaderRepository;
	
	@Autowired
	ConfigFormTypeService configFormTypeService;
	
	@Autowired
	ConfigFormFieldService configFormFieldService;
	
	@GetMapping
//	@Transactional
	public ResponseEntity<TableResultResponse<ConfigFormType>> list() {
		List<ConfigFormType> formTypies =configFormTypeRepository.findAll(Sort.by("cfId"));
//		List<ConfigFormType> formTypies =configFormTypeRepository.listAllNormalFormTypies();
		log.info("formTypies:{}", formTypies);
		return ResponseEntity.ok(new TableResultResponse<ConfigFormType>(formTypies));
	}
	
	@GetMapping("/createlist")
	public ResponseEntity<TableResultResponse<ConfigFormType>> createList() {
//		List<ConfigFormType> formTypies =configFormTypeRepository.findAll(Sort.by("cfId"));
		List<ConfigFormType> formTypies =configFormTypeRepository.listAllNormalFormTypies();
		return ResponseEntity.ok(new TableResultResponse<ConfigFormType>(formTypies));
	}
	
	@GetMapping("/noworkflowlist")
	public ResponseEntity<List<ConfigFormType>> noworkflowlist() {
//		List<ConfigFormType> formTypies =configFormTypeRepository.findAll(Sort.by("cfId"));
		List<ConfigFormType> formTypies =configFormTypeRepository.listAllFormTypiesWorkflowIsNull();
		return ResponseEntity.ok(formTypies);
	}
	
	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity<ConfigFormType> getById(@PathVariable("id") Long id) {
		ConfigFormType cFormType= configFormTypeRepository.getReferenceById(id);
		return ResponseEntity.ok(cFormType);
	}
	
//	@GetMapping("/{id}/fields")
//	public ResponseEntity<List<ConfigFormFieldResponse>> getFormTypeFields(@PathVariable("id") Long id) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
//		return ResponseEntity.ok(configFormTypeService.getFormTypeAllFields(id, true));
//	}
	
	@GetMapping("/{formTypeId}/visiblefields")
	public ResponseEntity<List<UserFormFieldResponse>> getFormVisibleFields(@PathVariable("formTypeId") Long formTypeId) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
		return ResponseEntity.ok(configFormTypeService.getFormTypeVisibleFields(formTypeId));
//		return ResponseEntity.ok(configFormTypeService.getFormTypeFieldsEnum(formTypeId));
//		return ResponseEntity.ok(configFormTypeService.getVisibleFields(formId));
	}
	
	@GetMapping("/{formTypeId}/tables")
	public ResponseEntity<Set<ConfigTableHeader>> getFormTables(@PathVariable("formTypeId") Long formTypeId) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
		return ResponseEntity.ok(configFormTypeService.getFormTypeAllTables(formTypeId));
//		return ResponseEntity.ok(configFormTypeService.getFormTypeFieldsEnum(formTypeId));
//		return ResponseEntity.ok(configFormTypeService.getVisibleFields(formId));
	}
	
	@GetMapping("/{formTypeId}/fields/{fieldIndex}")
	public ResponseEntity<List<ConfigListItem>> getFormFieldList(@PathVariable("formTypeId") Long formTypeId,@PathVariable("fieldIndex") String fieldIndex) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
		//TODO get field lists
		
		
		return ResponseEntity.ok(configFormFieldService.getFieldListItems(formTypeId, fieldIndex));
//		return ResponseEntity.ok(configFormTypeService.getFormTypeFieldsEnum(formTypeId));
//		return ResponseEntity.ok(configFormTypeService.getVisibleFields(formId));
	}
	
//	@GetMapping("/{id}/requiredfields")
//	public ResponseEntity<List<UserFormFieldResponse>> getFormTypeRequiredFields(@PathVariable("id") Long id) {
////		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
//		return ResponseEntity.ok(configFormTypeService.getFormTypeVisibleFields(id));
//	}
	
	@GetMapping("/{id}/visiblefieldsenum")
	public ResponseEntity<List<ConfigFormFieldEnum>> getFormTypeVisibleFieldsEnum(@PathVariable("id") Long id) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
		return ResponseEntity.ok(configFormTypeService.getFormTypeFieldsEnum(id));
	}
	
	@GetMapping("/{id}/fields")
	public ResponseEntity<TableResultResponse> getFormTypeAllFields(@PathVariable("id") Long id) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
		List<UserFormFieldResponse> allFields = configFormTypeService.getFormTypeAllFields(id);
		return ResponseEntity.ok(new TableResultResponse<UserFormFieldResponse>(allFields));
	}
	
	@GetMapping("/{id}/stepsfields")
	public ResponseEntity<List<List<UserFormFieldResponse>>> getStepsFormTypeFields(@PathVariable("id") Long id) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
		return ResponseEntity.ok(configFormTypeService.getStpesFormTypeAllFields(id));
	}
	
	@GetMapping("/{id}/steps")
	public ResponseEntity<TableResultResponse> getFormTypeAllSteps(@PathVariable("id") Long id) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
		List<ConfigStep> allSteps = configFormTypeService.getAllStep(id);
		return ResponseEntity.ok(new TableResultResponse<ConfigStep>(allSteps));
	}
	
	@GetMapping("/{id}/autonumbers")
	public ResponseEntity<TableResultResponse> getFormTypeAllAutoNumbers(@PathVariable("id") Long id) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
		List<ConfigFormNumber> allAutoNumbers = configFormTypeService.getAllAutoNumber(id);
		return ResponseEntity.ok(new TableResultResponse<ConfigFormNumber>(allAutoNumbers));
	}
	
	@PostMapping()
	@Operation(summary = "建立新表單類別",
               description = "創建新的表單")
	public ResponseEntity<ConfigFormType> create(@RequestBody @Validated ConfigFormTypeRequest formTypeReq){
		ConfigFormType newFormType = new ConfigFormType();
		newFormType.setName(formTypeReq.getName());
		newFormType.setDescription(formTypeReq.getDescription());
		
		if (formTypeReq.getCtIds() != null) {
			Set<ConfigTableHeader> cTableHeaders = new HashSet<>();
			for (Long ctId : formTypeReq.getCtIds()) {
				cTableHeaders.add(configTableHeaderRepository.getReferenceById(ctId));
			}
			newFormType.setConfigTableHeaders(cTableHeaders);
		}
		newFormType = configFormTypeService.createFormType(newFormType);
		//將多組FormNumber寫入FormType
		if (formTypeReq.getCfnIds() != null) {
			for(Long cfnId:formTypeReq.getCfnIds()) {
				ConfigFormNumber cfn = configFormNumberRepository.getReferenceById(cfnId);
				cfn.setConfigFormType(newFormType);
				configFormNumberRepository.save(cfn);
			}
		}
		return ResponseEntity.ok(newFormType);
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "刪除表單類別",
               description = "刪除表單類別")
	public ResponseEntity<Long> delete(@PathVariable("id") Long id){
		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
		if (formType == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			configFormTypeRepository.delete(formType);
			return new ResponseEntity<>(id, HttpStatus.OK);
		}
		
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "依id更新表單類別",
		       description = "透過id更新表單類別的資料")
	public ResponseEntity<ConfigFormType> updateFormTypeById(@PathVariable("id") Long id, @RequestBody ConfigFormTypeRequest formTypeReq) {
		ConfigFormType oldFormType = configFormTypeRepository.getReferenceById(id);
		ConfigFormType newFormType = oldFormType;
		ConfigWorkflow configWorkflow=null;
		Long cwId = formTypeReq.getCwId();
		if (oldFormType == null) {
			throw new EntityNotFoundException("Form Type "+formTypeReq.getName()+" not found!"); 
		}
		

		if (cwId!=null) {
			configWorkflow = configWorkflowRepository.getReferenceById(cwId);
			newFormType.setConfigWorkflow(configWorkflow);
		}
		newFormType.setDescription(formTypeReq.getDescription());
		newFormType.setName(formTypeReq.getName());
//		newFormType.setConfigFormNumbers(null);
		Set<ConfigTableHeader> cTableHeaders = new HashSet<>();
		for (Long ctId : formTypeReq.getCtIds()) {
			cTableHeaders.add(configTableHeaderRepository.getReferenceById(ctId));
		}
		newFormType.setConfigTableHeaders(cTableHeaders);
		
		newFormType = configFormTypeRepository.save(newFormType);
		//先將原先FormNumber清除
		for(ConfigFormNumber cformNumber:oldFormType.getConfigFormNumbers()) {
			log.info("clear auto number");
//			System.out.println("clear auto number");
			cformNumber.setConfigFormType(null);
			configFormNumberRepository.save(cformNumber);
		}
		
		//將多組FormNumber寫入FormType
		for(Long cfnId:formTypeReq.getCfnIds()) {
			ConfigFormNumber cfn = configFormNumberRepository.getReferenceById(cfnId);
			cfn.setConfigFormType(newFormType);
			configFormNumberRepository.save(cfn);
		}
		
		return ResponseEntity.ok(newFormType);
	}

}
