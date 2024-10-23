package com.miniplm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.ConfigTableHeader;
import com.miniplm.entity.Form;
import com.miniplm.entity.TableData;
import com.miniplm.entity.ZAccount;

public interface TableDataRepository extends JpaRepository<TableData, Long>{
//	List<TableData> findByForm(Form form);
//	Page<TableData> findByForm(Form form, Pageable pageable);
	List<TableData> findByFormAndConfigTableHeader(Form form, ConfigTableHeader cTableHeader);
	Page<TableData> findByFormAndConfigTableHeader(Form form, ConfigTableHeader cTableHeader, Pageable pageable);
}
