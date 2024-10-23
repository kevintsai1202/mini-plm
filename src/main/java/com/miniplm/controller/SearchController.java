package com.miniplm.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigCriteriaItem;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.Form;
import com.miniplm.repository.ConfigCriteriaNodeRepository;
import com.miniplm.response.FormResponse;
import com.miniplm.response.TableResultResponse;
import com.miniplm.service.FormDetailsService;
import com.miniplm.service.QueryService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/search")
@CrossOrigin
@Slf4j
public class SearchController {
	
	@Resource
	private ConfigCriteriaNodeRepository configCriteriaNodeRepository;
	@Autowired
	private FormDetailsService formDetailsService;
	@Autowired
	private QueryService queryService;
	
	@GetMapping("/quicksearch/{keyword}")
	@Operation(summary = "依 keyword 搜尋返回清單",
		       description = "依 keyword 搜尋返回清單")
	public ResponseEntity<TableResultResponse<Page<FormResponse>>> quickSearch(@PathVariable("keyword") String keyword, Pageable pageable) {
		return ResponseEntity.ok(new TableResultResponse(formDetailsService.quickSearch(keyword, pageable)));
	}
	
	@PostMapping("/advancesearch/{formTypeId}")
	@Operation(summary = "依 Criteria items 搜尋返回清單",
		       description = "依 Criteria items 搜尋返回清單")
	public ResponseEntity<TableResultResponse<List<Form>>> advanceSearch(@PathVariable("formTypeId") Long formTypeId, @RequestBody List<ConfigCriteriaItem> criteriaItems) {
		
		List<Form> forms = queryService.queryByCriteria(formTypeId, criteriaItems);
		
		List<FormResponse> formResponses = forms.stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
		
		return ResponseEntity.ok(new TableResultResponse(formResponses));
	}
	
	@PostMapping("/advancesearch/criterianode/{criteriaNodeId}")
	@Operation(summary = "依 Criteria items 搜尋返回清單",
		       description = "依 Criteria items 搜尋返回清單")
	public ResponseEntity<TableResultResponse<List<FormResponse>>> advanceSearch(@PathVariable("criteriaNodeId") Long criteriaNodeId) {
		ConfigCriteriaNode node = configCriteriaNodeRepository.getReferenceById(criteriaNodeId);
		
		List<Form> forms = queryService.queryByCriteria(node);
		
		List<FormResponse> formResponses = forms.stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
		
		return ResponseEntity.ok(new TableResultResponse(formResponses));
	}


}
