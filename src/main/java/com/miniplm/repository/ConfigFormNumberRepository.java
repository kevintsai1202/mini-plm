package com.miniplm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.ConfigFormNumber;

public interface ConfigFormNumberRepository extends JpaRepository<ConfigFormNumber, Long> {
	
//	@Query(value = "select * from MP_CONFIG_FORM_NUMBER where ENABLED = 1" ,nativeQuery = true)
//	public List<ConfigFormNumber> quickListAll();
}
