package com.miniplm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.entity.ConfigTableHeader;
import com.miniplm.entity.Form;
import com.miniplm.entity.TableData;
import com.miniplm.repository.ConfigListItemRepository;
import com.miniplm.repository.ConfigListNodeRepository;
import com.miniplm.repository.ConfigTableHeaderRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.repository.TableDataRepository;
import com.miniplm.request.TableDataRequest;
import com.miniplm.response.TableDataResponse;
import com.miniplm.response.TableResultResponse;
import com.miniplm.service.TableService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "下拉選單" , description = "與下拉選單相關的API")
@RestController
@RequestMapping({"/api/v1/tabledatas"})
@CrossOrigin
@Slf4j
public class TableController {
	@Resource
	private TableDataRepository tableDataRepository;
	@Resource
	private FormRepository formRepository;
	@Resource
	private ConfigTableHeaderRepository configTableHeaderRepository;
	
	@Autowired
	private TableService tableService;
	
	@GetMapping("/form/{formId}/header/{cTableHeaderId}")
	@Operation(summary = "取得Tabledata列表",
		       description = "取得Tabledata列表")
	public ResponseEntity<TableResultResponse> FindByFormAndTable(@PathVariable("formId") Long formId, @PathVariable("cTableHeaderId") Long cTableHeaderId, Pageable pageable) {
		List<TableData> tableDatas= tableService.getTableDatas(formId, cTableHeaderId);
		return ResponseEntity.ok(new TableResultResponse(tableDatas));
	}
	
	@PostMapping("/form/{formId}/header/{cTableHeaderId}")
	@Operation(summary = "建立新Table Data",
               description = "建立新Table Data")
	public ResponseEntity<TableData> create(@PathVariable("formId") Long formId, @PathVariable("cTableHeaderId") Long cTableHeaderId, @RequestBody @Validated TableDataRequest tableDataReq){
		return ResponseEntity.ok(tableService.saveTableData(formId, cTableHeaderId, tableDataReq));
	}
	
	@PutMapping("/form/{formId}/header/{cTableHeaderId}")
	@Operation(summary = "更新Table Data",
               description = "更新Table Data")
	public ResponseEntity<TableData> update(@PathVariable("formId") Long formId, @PathVariable("cTableHeaderId") Long cTableHeaderId, @RequestBody @Validated TableDataRequest tableDataReq){
		return ResponseEntity.ok(tableService.updateTableData(tableDataReq));
	}
	
	@DeleteMapping("/{tdId}")
	@Operation(summary = "刪除Table Data",
               description = "刪除Table Data")
	public ResponseEntity<String> delete(@PathVariable("tdId") Long tdId){
		tableService.deleteTableData(tdId);
		return ResponseEntity.ok("Delete Success");
	}
	
}
