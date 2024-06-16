package com.miniplm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigFormNumber;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigStepCriteria;
import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.entity.Form;
import com.miniplm.repository.ConfigFormFieldRepository;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.repository.ConfigWorkflowRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.response.ConfigFormFieldEnum;
import com.miniplm.response.UserFormFieldResponse;

@Service
public class ConfigFormTypeService {
	@Resource
	ConfigFormTypeRepository configFormTypeRepository;
	@Resource
	ConfigFormNumberRepository configFormNumberRepository;
	@Resource
	ConfigWorkflowRepository configWorkflowRepository;
	@Resource
	ConfigFormFieldRepository configFormFieldRepository;
	@Resource
	FormRepository formRepository;
	@Autowired
	QueryService queryService;
	@Autowired
	FormDetailsService formDetailsService;
	@Autowired
	AuthorizationService authorizationService;
	
	
	@Transactional
	public void setWorkflow(Long formTypeId, Long workflowId) {
		ConfigFormType formType = null;
		ConfigWorkflow workflow = null;
		
		formType = configFormTypeRepository.getReferenceById(formTypeId);
		workflow = configWorkflowRepository.getReferenceById(workflowId);
		
		formType.setConfigWorkflow(workflow);
		configFormTypeRepository.save(formType);
	}
	
	@Transactional
	public void setFormNumbers(Long formTypeId, List<Long> formNumberIds) {
		ConfigFormType formType = null;
		List<ConfigFormNumber> formNumbers = new LinkedList<>();
		
		formType = configFormTypeRepository.getReferenceById(formTypeId);
		
		formNumberIds.forEach(formNumberId -> formNumbers.add(configFormNumberRepository.getReferenceById(formNumberId)));		
		
		formType.setConfigFormNumbers(formNumbers);
		configFormTypeRepository.save(formType);
	}
	
	@Transactional
	public ConfigFormType getFormType(Long formTypeId) {
		ConfigFormType formType = null;
		formType = configFormTypeRepository.getReferenceById(formTypeId);
		return formType;
	}
	
	@Transactional
	public ConfigFormType createFormType(ConfigFormType formType) {
		ConfigFormType newFormType = null;
		newFormType = configFormTypeRepository.save(formType);
		int orderBy = 1;
		//TODO 建立所有欄位設定
		List groupList = new ArrayList();
		for (int i = 1 ; i <=10 ; i++) {
			String sDataIndex = "group"+String.format("%02d", i);
			groupList.add(new ConfigFormField("group", sDataIndex, orderBy++, newFormType));
		}
		
		List textList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "text"+String.format("%02d", i);
			textList.add(new ConfigFormField("text", sDataIndex, orderBy++, newFormType));
		}
		
		List textareaList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "textarea"+String.format("%02d", i);
			textareaList.add(new ConfigFormField("textarea", sDataIndex, orderBy++, newFormType));
		}
		
		List dateList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "date"+String.format("%02d", i);
			dateList.add(new ConfigFormField("date", sDataIndex, orderBy++, newFormType));
		}
		
		List selectList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "select"+String.format("%02d", i);
			selectList.add(new ConfigFormField("select", sDataIndex, orderBy++, newFormType));
		}
		
		List multilistList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "multilist"+String.format("%02d", i);
			multilistList.add(new ConfigFormField("multilist", sDataIndex, orderBy++, newFormType, true));
		}
		
		List checkboxList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "checkbox"+String.format("%02d", i);
			checkboxList.add(new ConfigFormField("checkbox", sDataIndex, orderBy++, newFormType, true));
		}
		
		List radioList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "radio"+String.format("%02d", i);
			radioList.add(new ConfigFormField("radio", sDataIndex, orderBy++, newFormType));
		}
		
		List numberList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "number"+String.format("%02d", i);
			numberList.add(new ConfigFormField("digit", sDataIndex, orderBy++, newFormType));
		}
		
		List imageList = new ArrayList();
		for (int i = 1 ; i <=05 ; i++) {
			String sDataIndex = "image"+String.format("%02d", i);
			imageList.add(new ConfigFormField("image", sDataIndex, orderBy++, newFormType));
		}
		
		List uploadList = new ArrayList();
		for (int i = 1 ; i <=05 ; i++) {
			String sDataIndex = "upload"+String.format("%02d", i);
			uploadList.add(new ConfigFormField("upload", sDataIndex, orderBy++, newFormType));
		}
		
		List htmlList = new ArrayList();
		for (int i = 1 ; i <=10 ; i++) {
			String sDataIndex = "html"+String.format("%02d", i);
			uploadList.add(new ConfigFormField("html", sDataIndex, orderBy++, newFormType));
		}
		
		configFormFieldRepository.saveAll(groupList);
		configFormFieldRepository.saveAll(textList);
		configFormFieldRepository.saveAll(textareaList);
		configFormFieldRepository.saveAll(dateList);
		configFormFieldRepository.saveAll(selectList);
		configFormFieldRepository.saveAll(multilistList);
		configFormFieldRepository.saveAll(checkboxList);
		configFormFieldRepository.saveAll(radioList);
		configFormFieldRepository.saveAll(numberList);
		configFormFieldRepository.saveAll(imageList);
		configFormFieldRepository.saveAll(uploadList);
		configFormFieldRepository.saveAll(htmlList);
		return newFormType;
	}
	
	public List<UserFormFieldResponse> getFormTypeAllFields(Long formTypeId){
		List<UserFormFieldResponse> allFields = new ArrayList<>();
		ConfigFormType formType = configFormTypeRepository.getReferenceById(formTypeId);
		List<ConfigFormField> groupFields = configFormFieldRepository.getFormTypeGroups(formTypeId);
		Map<String, UserFormFieldResponse> groups = new HashMap();
		
		for (ConfigFormField groupField : groupFields) {
			groups.put(groupField.getDataIndex(), new UserFormFieldResponse(groupField.getFieldName(), groupField.getDataIndex()));
		}
		
		List<ConfigFormField> fields = formType.getConfigFormFields();
		for (ConfigFormField field:fields) {
//			if (field.getVisible()) {
				if (field.getGroups() == null) {
					allFields.add(new UserFormFieldResponse(field, false));
				}else {
					UserFormFieldResponse groupField = groups.get(field.getGroups());
					groupField.getColumns().add(new UserFormFieldResponse(field,false));
					if (!allFields.contains(groupField)) {
						allFields.add(groupField);
					}
				}
//			}
		}
		return allFields;
	}
	
	public List<UserFormFieldResponse> getVisibleFields(Long formId){
		List<UserFormFieldResponse> allFields = new ArrayList<>();
		Form form = formRepository.getReferenceById(formId);
		ConfigStep currStep = form.getCurrStep();
		ConfigFormType formType = form.getConfigFormType();
		ConfigStep firstStep = formDetailsService.getFirstStep(form);
		
		if (currStep.equals(firstStep)) {
			allFields = getFormTypeVisibleFields(formType.getCfId());
		}else {
			allFields = getStepRequiredFields(formId);
		}
		return allFields;
	}
	
//	@Transactional
	public List<UserFormFieldResponse> getFormTypeVisibleFields(Long formTypeId){
		List<UserFormFieldResponse> allFields = new ArrayList<>();
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(formTypeId);
		List<ConfigFormField> groupFields = configFormFieldRepository.getFormTypeGroups(formTypeId);
		Map<String, UserFormFieldResponse> groups = new HashMap();
		
		for (ConfigFormField groupField : groupFields) {
			groups.put(groupField.getDataIndex() , new UserFormFieldResponse(groupField.getFieldName(), groupField.getDataIndex()));
		}
		//TODO 改直接抓ConfigFormFieldRepository
		List<ConfigFormField> fields = configFormFieldRepository.findByFormTypeAndVisible(formTypeId);
//		List<ConfigFormField> fields = formType.getConfigFormFields();
		for (ConfigFormField field:fields) {
			boolean canModify = authorizationService.matchMyModifyFields(formTypeId, field.getDataIndex());
//			if (field.getVisible()) {
				if (field.getGroups() == null) {
					allFields.add(new UserFormFieldResponse(formTypeId, field, false, canModify));
				}else {
					UserFormFieldResponse groupField = groups.get(field.getGroups());
					groupField.getColumns().add(new UserFormFieldResponse(formTypeId, field, false, canModify));
					if (!allFields.contains(groupField)) {
						allFields.add(groupField);
					}
				}
//			}
		}
		return allFields;
	}
	
	
	public List<UserFormFieldResponse> getStepRequiredFields(Long formId){
		Form form = formRepository.getReferenceById(formId);
		ConfigStep currStep = form.getCurrStep();
		List<ConfigStepCriteria> StepCriterias = currStep.getCStepCriterias();
		Set<Object> requiredFieldObjs = new HashSet<>();
		
		for (ConfigStepCriteria stepCriteria : StepCriterias) {
			if (queryService.matchFormByCriteria(formId, stepCriteria.getCCriteriaNode())) {
				requiredFieldObjs.addAll(stepCriteria.getRequiredFields());
			}
		}
		
		Set<String> requiredFields = requiredFieldObjs.stream().map(Object::toString).collect(Collectors.toSet());
		
		ConfigFormType formType = form.getConfigFormType();
		Long formTypeId = formType.getCfId();
		List<UserFormFieldResponse> allFields = new ArrayList<>();
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(formTypeId);
		List<ConfigFormField> groupFields = configFormFieldRepository.getFormTypeGroups(formTypeId);
		Map<String, UserFormFieldResponse> groups = new HashMap();
		
		for (ConfigFormField groupField : groupFields) {
			groups.put(groupField.getDataIndex() , new UserFormFieldResponse(groupField.getFieldName(), groupField.getDataIndex()));
		}
		//TODO 改直接抓ConfigFormFieldRepository
		List<ConfigFormField> fields = configFormFieldRepository.findByFormTypeAndVisible(formTypeId);
//		List<ConfigFormField> fields = formType.getConfigFormFields();
		for (ConfigFormField field:fields) {
			boolean canModify = authorizationService.matchMyModifyFields(formTypeId, field.getDataIndex());
//			if (field.getVisible()) {
				if (field.getGroups() == null) {
					if (requiredFields.contains(field.getDataIndex()))
						allFields.add(new UserFormFieldResponse(formTypeId, field, true, canModify));
					else 
						allFields.add(new UserFormFieldResponse(formTypeId, field, false, canModify));
				}else {
					UserFormFieldResponse groupField = groups.get(field.getGroups());
					if (requiredFields.contains(field.getDataIndex()))
						groupField.getColumns().add(new UserFormFieldResponse(formTypeId, field,true, canModify));
					else
						groupField.getColumns().add(new UserFormFieldResponse(formTypeId, field,false, canModify));
					if (!allFields.contains(groupField)) {
						allFields.add(groupField);
					}
				}
//			}
		}
		return allFields;
	}
	
	public List<ConfigFormFieldEnum> getFormTypeFieldsEnum(Long formTypeId){
		List<ConfigFormFieldEnum> enumFields = new ArrayList<>();
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(formTypeId);
//		List<String> sGroups = configFormFieldRepository.getFormTypeGroups(formTypeId);
//		Map<String, UserFormFieldResponse> groups = new HashMap();
//		
//		for (String sGroup : sGroups) {
//			groups.put(sGroup, new UserFormFieldResponse(sGroup));
//		}
		
		List<ConfigFormField> fields = configFormFieldRepository.findByFormTypeAndVisible(formTypeId);
		for (ConfigFormField field:fields) {
			enumFields.add(new ConfigFormFieldEnum(field));

		}
		return enumFields;
	}
	
	public List<List<UserFormFieldResponse>> getStpesFormTypeAllFields(Long formTypeId){
		List stepsFields = new ArrayList();
		List<UserFormFieldResponse> allFields = new ArrayList<>();
		ConfigFormType formType = configFormTypeRepository.getReferenceById(formTypeId);
		List<ConfigFormField> fields = formType.getConfigFormFields();
		for (ConfigFormField field:fields) {
			boolean canModify = authorizationService.matchMyModifyFields(formTypeId, field.getDataIndex());
			if (field.getVisible().equals(true)) {
				allFields.add(new UserFormFieldResponse(formTypeId, field, false, canModify));
			}
		}
		stepsFields.add(allFields);
		stepsFields.add(new ArrayList());
		stepsFields.add(new ArrayList());
		return stepsFields;
	}
	
	public List<ConfigStep> getAllStep(Long formTypeId){
		List configSteps = new ArrayList();
		Optional<ConfigFormType> op =  configFormTypeRepository.findById(formTypeId);
		ConfigFormType cft = op.get();
		ConfigWorkflow cwf = cft.getConfigWorkflow();
		configSteps = cwf.getCSteps();
		return configSteps;
	}
	
	public List<ConfigFormNumber> getAllAutoNumber(Long formTypeId){
		ConfigFormType cft =  configFormTypeRepository.getReferenceById(formTypeId);
		return cft.getConfigFormNumbers();
	}
}
