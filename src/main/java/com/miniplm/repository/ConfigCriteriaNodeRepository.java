package com.miniplm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigFormType;

public interface ConfigCriteriaNodeRepository extends JpaRepository<ConfigCriteriaNode, Long> {
	public List<ConfigCriteriaNode> findByConfigFormType(ConfigFormType cFormType);
}
