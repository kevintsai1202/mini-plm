package com.miniplm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.entity.ConfigTableColumn;
import com.miniplm.repository.ConfigFormFieldRepository;
import com.miniplm.repository.ConfigListNodeRepository;
import com.miniplm.repository.ConfigTableColumnRepository;
import com.miniplm.request.ConfigFormFieldRequest;
import com.miniplm.request.ConfigTableColumnRequest;
import com.miniplm.response.TableResultResponse;
import com.miniplm.response.UserFormFieldResponse;
import com.miniplm.response.UserTableColumnResponse;
import com.miniplm.service.ConfigFormFieldService;
import com.miniplm.service.ConfigTableColumnService;
import com.miniplm.service.ConfigTableService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "ConfigTableColumn" , description = "與Config Table Column相關的API")
@RestController
@RequestMapping({"/api/v1/admin/config/tablecolumns", "/api/v1/config/tablecolumns"})
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class ConfigTableColumnController {
	private final ConfigTableColumnRepository configTableColumnRepository;
	
	private final ConfigListNodeRepository configListNodeRepository;
	
	private final ConfigTableColumnService configTableColumnService;
	
	private final ConfigTableService configTableService;
	
	@GetMapping("/{id}")
	public ResponseEntity<UserTableColumnResponse> getById(@PathVariable("id") Long id) {
		ConfigTableColumn cTableColumn = configTableColumnRepository.getReferenceById(id);
		return ResponseEntity.ok(new UserTableColumnResponse(cTableColumn,false));
	}
	
	@PutMapping("/{id}/switchvisible")
	public ResponseEntity<UserTableColumnResponse> switchVisible(@PathVariable("id") Long id) {
		log.info("Switch Visible id: {}", id);
		ConfigTableColumn cTableColumn = configTableColumnService.switchVisible(id);
		return ResponseEntity.ok(new UserTableColumnResponse(cTableColumn,false));
	}
	
	@PutMapping("/{id}/switchrequired")
	public ResponseEntity<UserTableColumnResponse> switchRequired(@PathVariable("id") Long id) {
		log.info("Switch Required id: {}", id);
		ConfigTableColumn cTableColumn = configTableColumnService.switchRequired(id);
		return ResponseEntity.ok(new UserTableColumnResponse(cTableColumn,false));
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "依id更新Column設定",
		       description = "依id更新Column設定")
	public ResponseEntity<ConfigTableColumn> updateTableColumnById(@PathVariable("id") Long id, @RequestBody ConfigTableColumnRequest cTableColumnReq) {
		ConfigTableColumn oldColumn = configTableColumnRepository.getReferenceById(id);
		if (oldColumn != null) {
			Long clnId = cTableColumnReq.getClnId();
			if (clnId != null) {
				ConfigListNode cln = configListNodeRepository.getReferenceById(clnId);
				oldColumn.setConfigListNode(cln);
			}
			oldColumn.setColName(cTableColumnReq.getColName());
			oldColumn.setOrderBy(cTableColumnReq.getOrderBy());
			oldColumn.setPattern(cTableColumnReq.getPattern());
			oldColumn.setPatternMsg(cTableColumnReq.getPatternMsg());
			return ResponseEntity.ok(configTableColumnRepository.save(oldColumn));
		}
		else
			throw new EntityNotFoundException();
	}
	
	@PutMapping("/order")
	@Operation(summary = "修改Column的順序",
		       description = "修改Column的順序")
	public ResponseEntity<List<ConfigTableColumn>> updateColumnsOrder(@RequestBody List<ConfigTableColumnRequest> cTableColumnReqs) {
		
		List<ConfigTableColumn> updateColumns = new ArrayList<>(); 
		
		cTableColumnReqs.forEach(column->{
			ConfigTableColumn dbColumn = configTableColumnRepository.getReferenceById(column.getCtcId());
			dbColumn.setOrderBy(column.getOrderBy());
			updateColumns.add(dbColumn);
		});
		
		return ResponseEntity.ok(configTableColumnRepository.saveAll(updateColumns));
	}
	
	
	@GetMapping("/configtableheader/{cTableHeaderId}")
	public ResponseEntity<TableResultResponse> getTableVisibleColumns(@PathVariable("cTableHeaderId") Long cTableHeaderId) {
		log.info("cTableHeaderId: {}", cTableHeaderId);

		List<ConfigTableColumn> columns = configTableColumnService.getTableVisibleColumns(cTableHeaderId);
		return ResponseEntity.ok(new TableResultResponse<ConfigTableColumn>(columns));
	}
	
	@GetMapping("/configtableheader/{cTableHeaderId}/config")
	public ResponseEntity<TableResultResponse> getTableAllColumns(@PathVariable("cTableHeaderId") Long cTableHeaderId) {
		log.info("cTableHeaderId: {}", cTableHeaderId);

		List<ConfigTableColumn> columns = configTableColumnService.getTableAllColumns(cTableHeaderId);
		return ResponseEntity.ok(new TableResultResponse<ConfigTableColumn>(columns));
	}
	
}
