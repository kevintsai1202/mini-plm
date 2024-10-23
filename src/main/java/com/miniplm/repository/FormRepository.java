package com.miniplm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.Form;
import com.miniplm.entity.ZAccount;

public interface FormRepository extends JpaRepository<Form, Long>, JpaSpecificationExecutor<Form> {
	Form findByFormNumber(String formNumber);
	List<Form> findByConfigFormType(ConfigFormType configFormType);
	List<Form> findByCreator(ZAccount owner);
	Page<Form> findByConfigFormType(ConfigFormType configFormType, Pageable pageable);
	Page<Form> findByCreator(ZAccount owner, Pageable pageable);
	Page<Form> findByFormNumberContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String formNumber, String desc, Pageable pageable);
	
//	@Query(value = "select * from MP_FORM where ENABLED = 1 and CREATOR_ID = :userId", nativeQuery = true)
//	List<Form> findByCreatorId(@Param("userId") String userId);
}
