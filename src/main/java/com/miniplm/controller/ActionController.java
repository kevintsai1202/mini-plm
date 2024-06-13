package com.miniplm.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.Form;
import com.miniplm.entity.ZAccount;
import com.miniplm.request.SignOffRequest;
import com.miniplm.response.ActionResponse;
import com.miniplm.response.MessageResponse;
import com.miniplm.response.TableResultResponse;
import com.miniplm.service.ActionService;
import com.miniplm.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/actions")
@CrossOrigin
public class ActionController {
//	@Resource
//	private ActionRepository actionRepository;
	
	@Autowired
	private ActionService actionService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/me")
	@Operation(summary = "取得自己的Actions列表",
		       description = "返回自己所有actions清單")
	public ResponseEntity<TableResultResponse<ActionResponse>> myActions() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		ZAccount user = (ZAccount) userService.loadUserByUsername(username);
		return ResponseEntity.ok(new TableResultResponse<ActionResponse>(actionService.listMyActions(user.getId())));
	}
	
	@GetMapping("/user/{id}")
	@Operation(summary = "取得user的Actions列表",
		       description = "返回user id的所有actions清單")
	public ResponseEntity<TableResultResponse<ActionResponse>> listActionsById(@PathVariable("id") String id) {
		return ResponseEntity.ok(new TableResultResponse<ActionResponse>(actionService.listMyActions(id)));
	}
	
	@GetMapping("/forms/{formId}")
	@Operation(summary = "取得form的Actions列表",
		       description = "返回form id的所有actions清單")
	public ResponseEntity<TableResultResponse<ActionResponse>> formActions(@PathVariable("formId") Long formId) {
		return ResponseEntity.ok(new TableResultResponse<ActionResponse>(actionService.listActionsByFormId(formId)));
	}
	
	@PostMapping("/{id}/signoff")
	@Operation(summary = "簽核",
		       description = "簽核")
	public ResponseEntity<Form> signoff(@PathVariable("id") Long actionId, @Valid @RequestBody SignOffRequest signOffRequest) {
			return ResponseEntity.ok(actionService.signOff(actionId, signOffRequest));
	}
	
	@GetMapping("/forms/{formId}/checkunfinishactions")
	@Operation(summary = "確認表單是否有未完成Actions",
		       description = "確認表單是否有未完成Actions")
	public ResponseEntity checkUnfinishActions(@PathVariable("formId") Long formId) {
		if (actionService.hasUnfinishActions(formId)) {
			return ResponseEntity.ok(new MessageResponse("Yes"));
		}else {
			return ResponseEntity.ok(new MessageResponse("No"));
		}
	}
	
//	@GetMapping("/forms/{formId}/initactions")
//	@Operation(summary = "初始化表單actions",
//		       description = "初始化表單actions")
//	public ResponseEntity<String> initactions(@PathVariable("formId") Long formId) {
//		actionService.manualInitActions(formId);
//		return ResponseEntity.ok("OK");
//	}
	
}
