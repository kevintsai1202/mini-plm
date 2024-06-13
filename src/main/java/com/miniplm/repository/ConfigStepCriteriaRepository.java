package com.miniplm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigStepCriteria;

public interface ConfigStepCriteriaRepository extends JpaRepository<ConfigStepCriteria, Long> {
	List<ConfigStepCriteria> findBycStep(ConfigStep cStep);
}
