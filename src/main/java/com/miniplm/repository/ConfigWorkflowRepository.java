package com.miniplm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.miniplm.entity.ConfigWorkflow;

public interface ConfigWorkflowRepository extends JpaRepository<ConfigWorkflow, Long> {
	@Query(value = "select * from MP_CONFIG_WORKFLOW where ENABLED = 1 and STATUS = 1" ,nativeQuery = true)
	public List<ConfigWorkflow> listOpenWorkflow();
}
