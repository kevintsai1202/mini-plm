package com.miniplm.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.miniplm.convert.TableDataConverter;
import com.miniplm.entity.ConfigTableHeader;
import com.miniplm.entity.Form;
import com.miniplm.entity.TableData;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.repository.ConfigTableHeaderRepository;
import com.miniplm.repository.FormHistoryRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.repository.TableDataRepository;
import com.miniplm.request.TableDataRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TableService {
	@Resource
	FormRepository formRepository;
	
	@Resource
	TableDataRepository tableDataRepository;
	
	@Resource
	ConfigTableHeaderRepository configTableHeaderRepository;
	
	@Resource
	ConfigFormTypeRepository configFormTypeRepository;
	
	@Resource
	FormHistoryRepository formHistoryRepository;

	@Transactional
	public Page<TableData> getTableDatas(Long formId, Long cTableHeaderId, Pageable pageable){
		Form form = formRepository.getReferenceById(formId);
		ConfigTableHeader cTableHeader = configTableHeaderRepository.getReferenceById(cTableHeaderId);
		return tableDataRepository.findByFormAndConfigTableHeader(form, cTableHeader, pageable);
	}
	
	@Transactional
	public List<TableData> getTableDatas(Long formId, Long cTableHeaderId){
		Form form = formRepository.getReferenceById(formId);
		ConfigTableHeader cTableHeader = configTableHeaderRepository.getReferenceById(cTableHeaderId);
		return tableDataRepository.findByFormAndConfigTableHeader(form, cTableHeader);
	}
	
	@Transactional
	public TableData saveTableData(Long formId, Long cTableHeaderId, TableDataRequest tableDataReq){
		
		TableData tableData = TableDataConverter.INSTANCT.requestToEntity(tableDataReq);
		Form form = formRepository.getReferenceById(formId);
		ConfigTableHeader cTableHeader = configTableHeaderRepository.getReferenceById(cTableHeaderId);
		tableData.setForm(form);
		tableData.setConfigTableHeader(cTableHeader);
		
		return tableDataRepository.save(tableData);
	}
	
	@Transactional
	public TableData updateTableData(TableDataRequest tableDataReq){
		log.info("Begin updateTableData");
//		TableData newTableData = TableDataConverter.INSTANCT.requestToEntity(tableDataReq);
//		log.info("newTableData:{}", newTableData);
		Optional<TableData> oTableData = tableDataRepository.findById(tableDataReq.getTdId());
		TableData dbTableData = oTableData.get();
		log.info("dbTableData:{}", dbTableData);
		BeanUtils.copyProperties(tableDataReq, dbTableData);
		log.info("Coyied dbTableData:{}", dbTableData);
		return tableDataRepository.save(dbTableData);
	}
	
	@Transactional
	public void deleteTableData(Long tdId){
		tableDataRepository.deleteById(tdId);
	}
}
