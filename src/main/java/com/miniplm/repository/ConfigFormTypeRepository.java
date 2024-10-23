package com.miniplm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.miniplm.entity.ConfigFormType;

public interface ConfigFormTypeRepository extends JpaRepository<ConfigFormType, Long> {
	
	@Query(value="SELECT * FROM MP_CONFIG_FORM_TYPE WHERE ENABLED = 1 AND CONFIG_WORKFLOW_ID IS NOT NULL AND CONFIG_FORM_NUMBER_ID IS NOT NULL",nativeQuery = true)
	List<ConfigFormType> listAllNormalFormTypies();
	
	@Query(value="SELECT * FROM MP_CONFIG_FORM_TYPE WHERE ENABLED = 1 AND CONFIG_WORKFLOW_ID IS NULL",nativeQuery = true)
	List<ConfigFormType> listAllFormTypiesWorkflowIsNull();
}
