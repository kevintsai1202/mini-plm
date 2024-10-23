package com.miniplm.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigTableColumn;
import com.miniplm.entity.ConfigTableHeader;
import com.miniplm.repository.ConfigTableColumnRepository;
import com.miniplm.repository.ConfigTableHeaderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigTableColumnService {
	private final ConfigTableColumnRepository configTableColumnRepository;
	private final ConfigTableHeaderRepository configTableHeaderRepository;
	
	@Transactional
	public ConfigTableColumn switchVisible(Long id) {
		ConfigTableColumn column = configTableColumnRepository.getReferenceById(id);
		log.info("Column visible: {}", column.getVisible());
		column.setVisible(!column.getVisible());
		return configTableColumnRepository.save(column);
	}
	
	@Transactional
	public ConfigTableColumn switchRequired(Long id) {
		ConfigTableColumn column = configTableColumnRepository.getReferenceById(id);
		log.info("Column required: {}", column.getRequired());
		column.setRequired(!column.getRequired());
		return configTableColumnRepository.save(column);
	}
	
	@Transactional
	public ConfigTableColumn updateConfigTableColumn(Long id, ConfigTableColumn cTableColumn) {
		ConfigTableColumn dbColumn = configTableColumnRepository.getReferenceById(id);
		dbColumn.setColName(cTableColumn.getColName());
		dbColumn.setConfigListNode(cTableColumn.getConfigListNode());
		dbColumn.setPattern(cTableColumn.getPattern());
		dbColumn.setPatternMsg(cTableColumn.getPatternMsg());
		return configTableColumnRepository.save(dbColumn);
	}
	
//	public List<ConfigListItem> getColumnListItems(Long tableId, String fieldIndex) {
//		ConfigTableColumn column = configTableColumnRepository.findByTableHeaderIdAndFieldIndex(tableId, fieldIndex);
//		return column.getConfigListNode().getListItems();
//	}	
	
	@Transactional
	public List<ConfigTableColumn> getTableVisibleColumns(Long tableHeaderId){
		ConfigTableHeader configTableHeader = configTableHeaderRepository.getReferenceById(tableHeaderId);
		Sort sort = Sort.by(Sort.Direction.ASC, "orderBy");
		List<ConfigTableColumn> configTableColumns = configTableColumnRepository.findByConfigTableHeaderAndVisible(configTableHeader, true, sort);
		return configTableColumns;
	}
	
	@Transactional
	public List<ConfigTableColumn> getTableAllColumns(Long tableHeaderId){
		ConfigTableHeader configTableHeader = configTableHeaderRepository.getReferenceById(tableHeaderId);
		Sort sort = Sort.by(Sort.Direction.DESC, "visible")
			   .and(Sort.by(Sort.Direction.ASC, "orderBy"));
		List<ConfigTableColumn> configTableColumns = configTableColumnRepository.findByConfigTableHeader(configTableHeader, sort);
		return configTableColumns;
	}
}
