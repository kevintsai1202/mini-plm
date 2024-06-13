package com.miniplm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.Form;

public interface FormRepository extends JpaRepository<Form, Long>, JpaSpecificationExecutor<Form> {
	Form findByFormNumber(String formNumber);
	List<Form> findByConfigFormType(ConfigFormType configFormType);
}
