package com.miniplm.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.DataTypeEnum;

public interface ConfigFormFieldRepository extends JpaRepository<ConfigFormField, Long> {
	
	
	@Query(value = "select distinct groups.* from MP_CONFIG_FORM_FIELD fields, MP_CONFIG_FORM_FIELD groups where fields.GROUPS = groups.data_index AND fields.ENABLED = 1 and fields.VISIBLE = 1 and fields.GROUPS is not null and fields.FORM_TYPE_ID = :id and groups.FORM_TYPE_ID = :id" ,nativeQuery = true)
//	@Query(value = "select distinct GROUPS from MP_CONFIG_FORM_FIELD where ENABLED = 1 and VISIBLE = 1 and GROUPS is not null and FORM_TYPE_ID = :id" ,nativeQuery = true)
	public List<ConfigFormField> getFormTypeGroups(@Param("id") Long id);
	
	public List<ConfigFormField> findByConfigFormTypeAndFieldTypeAndVisible(ConfigFormType formType, DataTypeEnum fieldType, Boolean visible);
	
	public List<ConfigFormField> findByGroups(String groups);
	
	@Query(value = "select * from MP_CONFIG_FORM_FIELD where ENABLED = 1 and FORM_TYPE_ID = :id order by VISIBLE DESC, ORDER_BY" ,nativeQuery = true)
	public List<ConfigFormField> findByFormType(@Param("id") Long id);
	
	@Query(value = "select * from MP_CONFIG_FORM_FIELD where ENABLED = 1 and FORM_TYPE_ID = :id and VISIBLE = 1 and field_type != 'group' order by ORDER_BY" ,nativeQuery = true)
	public List<ConfigFormField> findByFormTypeAndVisible(@Param("id") Long id);
	
	@Query(value = "select * from MP_CONFIG_FORM_FIELD where ENABLED = 1 and FORM_TYPE_ID = :formTypeId and FIELD_INDEX = :fieldIndex order by ORDER_BY" ,nativeQuery = true)
	public ConfigFormField findByFormTypeAndFieldIndex(@Param("formTypeId") Long formTypeId, @Param("fieldIndex") String fieldIndex);
}
