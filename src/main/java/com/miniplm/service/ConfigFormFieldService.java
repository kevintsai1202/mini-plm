package com.miniplm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.DataTypeEnum;
import com.miniplm.repository.ConfigFormFieldRepository;
import com.miniplm.repository.ConfigFormTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfigFormFieldService {
	@Resource
	ConfigFormFieldRepository configFormFieldRepository;
	@Resource
	ConfigFormTypeRepository configFormTypeRepository;
	
	
	@Transactional
	public ConfigFormField switchVisible(Long id) {
		ConfigFormField field = configFormFieldRepository.getReferenceById(id);
		log.info("visible: {}", field.getVisible());
//		System.out.println("visible:"+field.getVisible());
		field.setVisible(!field.getVisible());
		return configFormFieldRepository.save(field);
	}
	
	@Transactional
	public ConfigFormField switchRequired(Long id) {
		ConfigFormField field = configFormFieldRepository.getReferenceById(id);
		log.info("Required: {}",field.getRequired());
//		System.out.println("Required:"+field.getRequired());
		field.setRequired(!field.getRequired());
		return configFormFieldRepository.save(field);
	}
	
	public List<ConfigListItem> getFieldListItems(Long formTypeId, String fieldIndex) {
		
		ConfigFormField field = configFormFieldRepository.findByFormTypeAndFieldIndex(formTypeId, fieldIndex);
//		System.out.println("Required:"+field.getRequired());
		return field.getConfigListNode().getListItems();
	}
	
	
	public Map<String, String> getGroupList(Long formTypeId){
		ConfigFormType formType = configFormTypeRepository.getReferenceById(formTypeId);
		List<ConfigFormField> groups = configFormFieldRepository.findByConfigFormTypeAndFieldTypeAndVisible(formType, DataTypeEnum.group , true);
		Map<String, String> groupsMap = new HashMap<>();
		for(ConfigFormField group: groups) {
			groupsMap.put(group.getDataIndex(), group.getFieldName());
		}
		return groupsMap;
	}
//	@Transactional
//	public ConfigFormField switchMultiple(Long id) {
//		ConfigFormField field = configFormFieldRepository.getReferenceById(id);
//		System.out.println("Required:"+field.getMultiple());
//		field.setMultiple(!field.getMultiple());
//		return configFormFieldRepository.save(field);
//	}
}
