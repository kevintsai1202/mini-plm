package com.miniplm.controller;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigTableHeader;
import com.miniplm.repository.ConfigTableColumnRepository;
import com.miniplm.repository.ConfigTableHeaderRepository;
import com.miniplm.response.TableResultResponse;
import com.miniplm.service.ConfigTableService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "表格" , description = "與表格相關的API")
@RestController
@RequestMapping({"/api/v1/admin/config/tableheaders","/api/v1/config/tableheaders"})
@CrossOrigin
@RequiredArgsConstructor
public class ConfigTableController {

	private final ConfigTableHeaderRepository cTableHeaderRepository;
	private final ConfigTableColumnRepository cTableColumnRepository;
	private final ConfigTableService cTableService;
	
	@GetMapping()
	@Operation(summary = "取得表格列表",
		       description = "返回所有表格列表")
	public ResponseEntity<TableResultResponse> list() {
		return ResponseEntity.ok(new TableResultResponse(cTableHeaderRepository.findAll()));
	}
	
	@PostMapping()
	@Operation(summary = "建立新表格",
               description = "創建新的表格")
	public ResponseEntity<TableResultResponse> create(@RequestBody @Validated ConfigTableHeader tableHeader){
		return ResponseEntity.ok(new TableResultResponse(cTableService.createTable(tableHeader)));
	}
	
	@GetMapping(value = "/{id}")
	@Operation(summary = "取得表格內容",
               description = "依id返回表格內容")
	public ResponseEntity<ConfigTableHeader> detail(@PathVariable("id") Long id) {
		Optional<ConfigTableHeader> temp = cTableHeaderRepository.findById(id);
		ConfigTableHeader tableHeader=null;
		if (temp.isPresent()) {
			tableHeader = temp.get();
		}else {
			throw new EntityNotFoundException("no record found id="+id+" for TableHeader");
		}
		return ResponseEntity.ok(tableHeader);
	}
	
	
	@PutMapping(value = "/{id}")
	@Operation(summary = "更新表格內容描述",
               description = "更新表格資訊")
	public ResponseEntity<ConfigTableHeader> updateConfigTableHeader(@PathVariable("id") Long id, @RequestBody @Validated ConfigTableHeader configTableHeader) {
		ConfigTableHeader tableHeader = cTableHeaderRepository.getReferenceById(id);
		tableHeader.setDescription(configTableHeader.getDescription());
		tableHeader.setName(configTableHeader.getName());
		return ResponseEntity.ok(cTableHeaderRepository.save(tableHeader));
	}
	
}
