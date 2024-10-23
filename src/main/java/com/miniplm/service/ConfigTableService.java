package com.miniplm.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.miniplm.entity.ConfigTableColumn;
import com.miniplm.entity.ConfigTableHeader;
import com.miniplm.entity.DataTypeEnum;
import com.miniplm.repository.ConfigTableColumnRepository;
import com.miniplm.repository.ConfigTableHeaderRepository;
import com.miniplm.response.UserTableColumnResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigTableService {
	
	private final ConfigTableHeaderRepository configTableHeaderRepository;
	
	private final ConfigTableColumnRepository configTableColumnRepository;
	
	@Transactional
	public ConfigTableHeader createTable(ConfigTableHeader cTableHeader) {
		ConfigTableHeader newTableHeader = null;
		newTableHeader = configTableHeaderRepository.save(cTableHeader);
		int orderBy = 1;
		
		List textList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "text"+String.format("%02d", i);
			textList.add(new ConfigTableColumn(DataTypeEnum.text, sDataIndex, orderBy++, newTableHeader));
		}
		
		List textareaList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "textarea"+String.format("%02d", i);
			textareaList.add(new ConfigTableColumn(DataTypeEnum.textarea , sDataIndex, orderBy++, newTableHeader));
		}
		
		List dateList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "date"+String.format("%02d", i);
			dateList.add(new ConfigTableColumn(DataTypeEnum.date, sDataIndex, orderBy++, newTableHeader));
		}
		
		List selectList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "select"+String.format("%02d", i);
			selectList.add(new ConfigTableColumn(DataTypeEnum.select, sDataIndex, orderBy++, newTableHeader));
		}
		
		List multilistList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "multilist"+String.format("%02d", i);
			multilistList.add(new ConfigTableColumn(DataTypeEnum.multilist, sDataIndex, orderBy++, newTableHeader, true));
		}
		
		List checkboxList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "checkbox"+String.format("%02d", i);
			checkboxList.add(new ConfigTableColumn(DataTypeEnum.checkbox, sDataIndex, orderBy++, newTableHeader, true));
		}
		
		List radioList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "radio"+String.format("%02d", i);
			radioList.add(new ConfigTableColumn(DataTypeEnum.radio, sDataIndex, orderBy++, newTableHeader));
		}
		
		List numberList = new ArrayList();
		for (int i = 1 ; i <=30 ; i++) {
			String sDataIndex = "number"+String.format("%02d", i);
			numberList.add(new ConfigTableColumn(DataTypeEnum.digit, sDataIndex, orderBy++, newTableHeader));
		}
		configTableColumnRepository.saveAll(textList);
		configTableColumnRepository.saveAll(textareaList);
		configTableColumnRepository.saveAll(dateList);
		configTableColumnRepository.saveAll(selectList);
		configTableColumnRepository.saveAll(multilistList);
		configTableColumnRepository.saveAll(checkboxList);
		configTableColumnRepository.saveAll(radioList);
		configTableColumnRepository.saveAll(numberList);
		return newTableHeader;
	}


	
}
