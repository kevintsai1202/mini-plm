package com.miniplm.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigFormNumber;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.Form;
import com.miniplm.entity.FormData;
import com.miniplm.repository.ActionRepository;
import com.miniplm.repository.ConfigStepRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.request.ApproversRequest;
import com.miniplm.request.FormDataRequest;
import com.miniplm.request.FormRequest;
import com.miniplm.response.ActionResponse;
import com.miniplm.response.FormResponse;
import com.miniplm.response.MessageResponse;
import com.miniplm.response.TableResultResponse;
import com.miniplm.response.UserFormFieldResponse;
import com.miniplm.response.WorkflowResponse;
import com.miniplm.service.ActionService;
import com.miniplm.service.ConfigFormTypeService;
import com.miniplm.service.FormDetailsService;
import com.miniplm.service.FormStatusService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/forms")
@CrossOrigin
@Slf4j
public class FormController {
	@Resource
	private FormRepository formRepository;
	
	@Resource
	private ConfigStepRepository configStepRepository;
	
	@Autowired
	private ConfigFormTypeService configFormTypeService;
	
	@Autowired
	private FormDetailsService formDetailsService;
	
	@Autowired
	private FormStatusService formStatusService;
	
	@Autowired
	private ActionService actionService;
	
	@GetMapping()
	@Operation(summary = "取得Form列表",
		       description = "返回所有Form的List")
	public ResponseEntity<TableResultResponse<FormResponse>> list() {
		return ResponseEntity.ok(new TableResultResponse<FormResponse>(formDetailsService.listAllForm()));
	}
	
	@GetMapping("/myform")
	@Operation(summary = "取得我建立的Form列表",
		       description = "返回所有我建立的Form的List")
	public ResponseEntity<TableResultResponse<FormResponse>> myFormList() {
		return ResponseEntity.ok(new TableResultResponse<FormResponse>(formDetailsService.listMyForm()));
	}
	
	@GetMapping("/formtype/{formtypeid}")
	@Operation(summary = "取得FormType列表",
		       description = "依FormType返回所有表單清單")
	public ResponseEntity<TableResultResponse<FormResponse>> listByFormType(@PathVariable("formtypeid") Long formTypeId) {
		return ResponseEntity.ok(new TableResultResponse<FormResponse>(formDetailsService.listByFormTypeId(formTypeId)));
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "依id取得Form",
		       description = "透過id查詢Form的資料")
	public ResponseEntity<Form> getFormById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(formRepository.getReferenceById(id));
	}
	

	
	@GetMapping("/{id}/workflow")
	@Operation(summary = "依id取得Form的Workflow",
		       description = "透過id查詢Form的Workflow")
	public ResponseEntity<WorkflowResponse> getWorkflowByFormId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(formStatusService.getFormWorkflow(id));
	}
	
	@GetMapping("/{id}/startworkflow")
	@Operation(summary = "將流程送出",
		       description = "將流程送出,申請人操作")
	public ResponseEntity<ConfigStep> startWorkflow(@PathVariable("id") Long id) {
		
		return ResponseEntity.ok(formStatusService.changeToNextStep(id));
	}
	
	@GetMapping("/{id}/nextstep")
	@Operation(summary = "將流程送至下關",
		       description = "將流程強制送至下一關,admin操作")
	public ResponseEntity<ConfigStep> nextStatus(@PathVariable("id") Long id) {
		return ResponseEntity.ok(formStatusService.changeToNextStep(id));
	}
	
	@GetMapping("/{formId}/autochangestep")
	@Operation(summary = "將流程自動往下送",
		       description = "當沒有簽核者後自動將流程往下送")
	public ResponseEntity<ConfigStep> autoChange(@PathVariable("formId") Long formId) {
		Form form = formRepository.getReferenceById(formId);
		ConfigStep step = form.getCurrStep().getNextStep();
		return ResponseEntity.ok(formStatusService.autoChangeStep(form, step));
	}
	
	@GetMapping("/{id}/checkpending")
	@Operation(summary = "確認表單是否為Pending關卡",
		       description = "確認表單是否為Pending關卡")
	public ResponseEntity checkPending(@PathVariable("id") Long id) {
		Form form = formRepository.getReferenceById(id);
		ConfigStep currStep = form.getCurrStep();
		ConfigStep pendingStep = form.getCWorkflow().getCSteps().get(0);
		if (currStep == pendingStep) {
			return ResponseEntity.ok(new MessageResponse("Pending"));
		}else {
			return ResponseEntity.ok(new MessageResponse("Not Pending"));
		}
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "依id更新Form&FormData",
		       description = "透過id更新Form & FormData的資料，需傳入FormDataRequest json")
	public ResponseEntity<Form> updateFormDataById(@PathVariable("id") Long id, @RequestBody FormDataRequest formDataRequest) {
		log.info("formDataRequest: {}",formDataRequest);
//		System.out.println("formDataRequest"+formDataRequest);
		formDetailsService.updateFormData(id, formDataRequest);
		return ResponseEntity.ok(formRepository.getReferenceById(id));
	}
	
	@PostMapping("/formtype/{formtypeid}")
	@Operation(summary = "建立新Form",
		       description = "縣立新Form")
	public ResponseEntity<Form> createForm(@PathVariable("formtypeid") Long formTypeId, @RequestBody FormRequest formReq) {
		return ResponseEntity.ok(formDetailsService.createForm(formTypeId, formReq));
	}
	
	@GetMapping("/{formId}/visiblefields")
	public ResponseEntity<List<UserFormFieldResponse>> getFormVisibleFields(@PathVariable("formId") Long formId) {
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(id);
//		return ResponseEntity.ok(configFormTypeService.getFormTypeVisibleFields(id));
		return ResponseEntity.ok(configFormTypeService.getVisibleFields(formId));
	}
	
//	@GetMapping("/formnumber/{formNumber}")
//	@Operation(summary = "依Form Number取得Form",
//		       description = "透過Form Number查詢Form的資料")
//	public ResponseEntity<FormResponse> getFormByFormNumber(@PathVariable("formNumber") String formNumber) {
//		return ResponseEntity.ok(formDetailsService.getFormDetailsByFormNumber(formNumber));
//	}
	
	
	@GetMapping("/formnumber/{formNumber}")
	@Operation(summary = "依Form Number取得Form",
		       description = "透過Form Number查詢Form的資料")
	public ResponseEntity<Form> getFormByFormNumber(@PathVariable("formNumber") String formNumber) {
		return ResponseEntity.ok(formRepository.findByFormNumber(formNumber));
	}
	
	@PatchMapping("/formnumber/{formNumber}")
	@Operation(summary = "依Form Number修改Form Data資料",
		       description = "透過Form Number修改Form Data的資料")
	public ResponseEntity<FormData> modifyFormByFormNumber(@PathVariable("formNumber") String formNumber, @RequestBody FormDataRequest formDataReq ) {
//		Form from = formRepository.findByFormNumber(formNumber);
		return ResponseEntity.ok(formDetailsService.patchUpdateFormData(formNumber, formDataReq));
	}
	
	@PutMapping("/{formId}/addapprovers")
	@Operation(summary = "動態添加簽核者",
		       description = "依Form目前關卡添加簽核者")
	public ResponseEntity<List<ActionResponse>> addApprovers(@PathVariable("formId") Long formId, @RequestBody ApproversRequest approversRequest) {
		log.info("formId: {}", formId);
		log.info("approverIds: {}", approversRequest.getApproverIds());

		List<ActionResponse> addedActions = actionService.addApprovers(formId, approversRequest.getApproverIds());
		return ResponseEntity.ok(addedActions);
	}
	
}
