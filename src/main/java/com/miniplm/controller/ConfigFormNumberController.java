package com.miniplm.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigFormNumber;
import com.miniplm.exception.ConfigAlreadyUsedException;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.response.TableResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "表單編號規則設定" , description = "與表單編號相關的API")
@RestController
@RequestMapping({"/api/v1/admin/config/formnumbers","/api/v1/config/formnumbers"})
@CrossOrigin
public class ConfigFormNumberController {

	@Resource
	private ConfigFormNumberRepository configFormNumberRepository;
	
	@GetMapping()
	@Operation(summary = "取得表單編號列表",
		       description = "返回所有表單編號設定")
	public ResponseEntity<TableResultResponse<ConfigFormNumber>> list() {
		List<ConfigFormNumber> formNumbers = configFormNumberRepository.findAll(Sort.by("cfnId"));
//		List<ConfigFormNumberResponse> formNumbers = configFormNumberRepository.quickListAll();
		return ResponseEntity.ok(new TableResultResponse(formNumbers));
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "取得表單編號設定內容",
               description = "依id返回表單編號設定內容")
	public ResponseEntity<ConfigFormNumber> detail(@PathVariable("id") Long id) {
		return ResponseEntity.ok(configFormNumberRepository.getReferenceById(id));
	}
	
	@PostMapping()
	@Operation(summary = "建立新表單編號",
               description = "創建新的表單編號設定")
	public ResponseEntity<ConfigFormNumber> create(@RequestBody @Validated ConfigFormNumber formNumber){
		return ResponseEntity.ok(configFormNumberRepository.save(formNumber));
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "刪除表單編號",
               description = "刪除表單編號設定")
	public ResponseEntity<Long> delete(@PathVariable("id") Long id){
		ConfigFormNumber formNumber = configFormNumberRepository.getReferenceById(id);
		if (formNumber == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			configFormNumberRepository.delete(formNumber);
			return new ResponseEntity<>(id, HttpStatus.OK);
		}
		
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "依id更新Form Number",
		       description = "透過id更新Form Number的資料")
	public ResponseEntity<ConfigFormNumber> updateFormDataById(@PathVariable("id") Long id, @RequestBody ConfigFormNumber formNumber) {
		ConfigFormNumber oldFormNumber = configFormNumberRepository.getReferenceById(id);
		if (oldFormNumber.getCurrMid() != null) {
			throw new ConfigAlreadyUsedException("Form Number "+oldFormNumber.getPrefix()+" has used, can't update!"); 
		}else {
			oldFormNumber.setPrefix(formNumber.getPrefix());
			oldFormNumber.setMidfix(formNumber.getMidfix());
			oldFormNumber.setSuffix(formNumber.getSuffix());
			oldFormNumber.setSeqLength(formNumber.getSeqLength());
			oldFormNumber.setMidSeqReset(formNumber.getMidSeqReset());
			oldFormNumber.setDescription(formNumber.getDescription());
		}
		return ResponseEntity.ok(configFormNumberRepository.save(oldFormNumber));
	}
//	
//	@SneakyThrows
//	@DeleteMapping(value = "/{id}")
//	@Operation(summary = "刪除分類",
//               description = "依據id刪除分類")
//	public void delete(@PathVariable("id") Long id) {
//		categoryService.delete(id);
//	}
//	
//	@PostMapping(value = "/{caid}/contracts")
//	@Operation(summary = "建立合約範本",
//               description = "依據分類id創建該分類下的合約範本")
//	public ResponseEntity<ModelResponse> createTemplate(@PathVariable("caid") Long caid, @RequestBody @Validated ModelRequest contractRequest){
//		return ResponseEntity.ok(contractService.createTemplate(caid, contractRequest));
//	}
//	
//	@GetMapping(value = "/{caid}/contracts")
//	@Operation(summary = "取得分類下的合約範本列表",
//               description = "依據分類id取得該分類下所有合約範本列表")
//	public ResponseEntity<List<ModelResponse>> listTemplate(@PathVariable("caid") Long caid){
//		return ResponseEntity.ok(contractService.listCategoryModels(caid));
//	}
	
}
