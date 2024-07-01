package com.miniplm.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.miniplm.entity.Action;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.entity.Form;
import com.miniplm.entity.ZAccount;
import com.miniplm.exception.BusinessException;
import com.miniplm.repository.ActionRepository;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.repository.FormDataRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.repository.UserRepository;
import com.miniplm.request.SignOffRequest;
import com.miniplm.response.ActionResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActionService {
	@Resource
	FormRepository formRepository;

	@Resource
	FormDataRepository formDataRepository;

	@Resource
	UserRepository userRepository;

	@Resource
	ConfigFormTypeRepository configFormTypeRepository;

	@Resource
	ConfigFormNumberRepository configFormNumberRepository;

	@Resource
	ActionRepository actionRepository;

	@Autowired
	FormStatusService formStatusService;
	
	@Autowired
	QueryService queryService;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

//	public ConfigStep autoChangeStep(Form form, ConfigStep currStep){
//		Long cstepId = currStep.getCsId();
//		ConfigStep tempStep = currStep;
//		int countByApprovers = actionRepository.countApproversByFormIdAndStepId(form.getFId(), cstepId);
//		System.out.println("curr step name:" + currStep.getStepName());
//		System.out.println("countByApprovers:" + countByApprovers);
//
//		while ((countByApprovers == 0) && (tempStep.getNextStep() != null)) {
////		System.out.println("countByApprovers:"+countByApprovers);
////		form.setCurrStep(currStep.getNextStep());
//
////		System.out.println("tempStep:"+tempStep.getStepName());
//			tempStep = tempStep.getNextStep();
//			countByApprovers = actionRepository.countApproversByFormIdAndStepId(form.getFId(), tempStep.getCsId());
//
//			System.out.println("temp Step Mame:" + tempStep.getStepName());
//			System.out.println("countByApprovers:" + countByApprovers);
//			// Change Status
//			// System.out.println("Need Change Status");
//		}
//		System.out.println("Ststus Change to:" + tempStep.getStepName());
////		form.setCurrStep(tempStep);
//		return tempStep;
//	}

//	@Transactional
//	public void manualInitActions(Long formID) {
//		Form form = formRepository.getReferenceById(formID);
//
//		initActions(form);
//	}

	public Boolean needApprve(Long formId, Long stepId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new InternalAuthenticationServiceException("Can't get current user");
		}
		ZAccount me = (ZAccount) authentication.getPrincipal();
		List<Action> myActions =actionRepository.FindMyActionsByFormIdAndStepId(formId, stepId, me.getId());
		System.out.println(myActions);
		return (myActions.stream().filter((action) -> action.getType().equals("A")).count() > 0);
	}
	
	@Transactional
	public Form signOffByFormId(Long formId, SignOffRequest signOffRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new InternalAuthenticationServiceException("Can't get current user");
		}
		ZAccount me = (ZAccount) authentication.getPrincipal();
		Form form = formRepository.getReferenceById(formId);
		List<Action> myActions = actionRepository.FindMyActionsByFormIdAndStepId(formId, form.getCurrStep().getCsId(), me.getId());
		Action action = myActions.stream().findFirst().orElse(null);
		return signOff(action.getAId(), signOffRequest);
	}
	
	
	@Transactional
	public Form signOff(Long _actionId, SignOffRequest signOffRequest) {

		Long actionId = _actionId;
		String password = signOffRequest.getPassword();
		Boolean signoffType = signOffRequest.getSignoffType();
		String comments = signOffRequest.getComments();

		Action action = actionRepository.getReferenceById(actionId);
		if (action.getFinishFlag() != null && action.getFinishFlag()) {
			throw new BusinessException("Sign-off completed");
		}

		Form form = action.getForm();
		ConfigStep currStep = form.getCurrStep();
		log.info("currStep: {}", currStep);
//		System.out.println("currStep:" + currStep);
		ConfigStep actionStep = action.getConfigStep();
		log.info("actionStep: {}" , actionStep);
//		System.out.println("actionStep:" + actionStep);
		if (!currStep.equals(actionStep)) {
			throw new BusinessException("Step not match");
		}

		ZAccount user = action.getUser();
		String md5Password = "";
		md5Password = DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase();
		if (!md5Password.equals(user.getPassword())) {
			throw new BusinessException("Password Error");
		}
		List<Action> listApprovers = actionRepository.findByTypeAndFormAndConfigStepAndFinishFlag("A", form, currStep, false);
//		int countByApprovers = actionRepository.countApproversByFormIdAndStepId(form.getFId(), currStep.getCsId());
		int countByApprovers = listApprovers.size();
		log.info("Before Approve, approvers count:{}", countByApprovers);
		action.setSignoffType(signoffType);
		action.setComments(comments);
		action.setFinishFlag(true);
		action = actionRepository.saveAndFlush(action);
		
		ConfigStep tempStep = currStep;
		if (signoffType) {//approve
			listApprovers = actionRepository.findByTypeAndFormAndConfigStepAndFinishFlag("A", form, currStep, false);
			countByApprovers = listApprovers.size();
			
			log.info("After Approve, Approvers count:{}", countByApprovers);
			
			if (countByApprovers == 0) {
				ConfigStep newStep = formStatusService.autoChangeStep(form, currStep.getNextStep());
				log.info("Ststus Change to: {}", newStep.getStepName());
				form.setCurrStep(newStep);
			}
			/*
			 * 以下移至 autoChangeStep
			 * Long cstepId = currStep.getCsId();
			int countByApprovers = actionRepository.countApproversByFormIdAndStepId(form.getFId(), cstepId);
			System.out.println("curr Step Mame:" + currStep.getStepName());
			System.out.println("countByApprovers:" + countByApprovers);
			
//			ConfigStep tempStep = currStep;

			while ((countByApprovers == 0) && (tempStep.getNextStep() != null)) {
//			System.out.println("countByApprovers:"+countByApprovers);
//			form.setCurrStep(currStep.getNextStep());

//			System.out.println("tempStep:"+tempStep.getStepName());
				tempStep = tempStep.getNextStep();
				countByApprovers = actionRepository.countApproversByFormIdAndStepId(form.getFId(), tempStep.getCsId());

				System.out.println("temp Step Mame:" + tempStep.getStepName());
				System.out.println("countByApprovers:" + countByApprovers);
				// Change Status
				// System.out.println("Need Change Status");
			}*/
//				log.info("Step change to: {}" , newStep.getStepName());
	//			System.out.println("Step change to:" + newStep.getStepName());
//				form.setCurrStep(newStep);
//				actionRepository.updateCurrCanNoticeActions(form.getFId(), newStep.getCsId());
//			}
		}else {//reject
//			ConfigStep tempStep = currStep;
			ignoreAllActions(form.getFId());
			ConfigStep rejectStep = tempStep.getRejectStep();
			ConfigStep firstStep = ConfigWorkflowService.getFirstStep(form);
			if (rejectStep == null) {//沒設定Reject回到第一關
//				rejectStep = firstStep;
				log.info("Step change to first step: {}", firstStep.getStepName());
//				System.out.println("Step change to first step:" + firstStep.getStepName());
				form.setCurrStep(firstStep);
				returnToPending(form.getFId());
			}else {
				if ( rejectStep != firstStep){
//				int countByApprovers = actionRepository.countApproversByFormIdAndStepId(form.getFId(), rejectStep.getCsId());
//				
//				form.setCurrStep(rejectStep);
//				System.out.println("Ststus Change to reject step:" + rejectStep.getStepName());
					ConfigStep newStep = formStatusService.autoChangeStep(form, rejectStep);
					log.info("Step change to:" + newStep.getStepName());
//					System.out.println("Step change to:" + newStep.getStepName());
					form.setCurrStep(newStep);
					actionRepository.updateCurrCanNoticeActions(form.getFId(), newStep.getCsId());
				}else {
//					ConfigStep newStep = formStatusService.autoChangeStep(form, rejectStep);
					log.info("Ststus Change to: {}", firstStep.getStepName());
//					System.out.println("Ststus Change to:" + firstStep.getStepName());
					form.setCurrStep(firstStep);
					returnToPending(form.getFId());
				}
				//將目前關卡後的Action重建
				
			}
//			actionService.initActions(form);
		}
		return formRepository.save(form);
	}

	public void ignoreStepActions(Long formId, Long stepId) {
		actionRepository.updateCurrUnfinishActions(formId, stepId);
	}

	public void ignoreAllActions(Long formId) {
		actionRepository.updateAllUnfinishActions(formId);
	}

	public boolean hasUnfinishActions(Long formId) {
		int count = actionRepository.countAllUnfinishActions(formId);
		if (count > 0)
			return true;
		else
			return false;
	}

//	public boolean needApprove(Long formId, Long userId) {
//		boolean needApprove = false;
//		Form form = formRepository.getReferenceById(formId);
//		List<Action> actions = form.getActions();
//		for (Action action : actions) {
//			ConfigStep actionStep = action.getConfigStep();
////			System.out.println("actionStep:"+actionStep);
//			ConfigStep currStep = form.getCurrStep();
//			if (currStep.equals(actionStep) && action.getFinishFlag() == null) {
//				needApprove = true;
//				break;
//			}
//		}
//		return needApprove;
//	}

	public List<ActionResponse> listMyActions(String userId) {
		log.info("userId: {}" , userId);
//		System.out.println("userId:" + userId);
		List<ActionResponse> results = new ArrayList<ActionResponse>();
		List<Action> source = actionRepository.findByApprover(userId);
		for (Action action : source) {
//			ConfigStep actionStep = action.getConfigStep();
//			System.out.println("actionStep:"+actionStep);
//			ConfigStep currStep = action.getForm().getCurrStep();
//			System.out.println("currStep:"+currStep);
//			if (currStep.equals(actionStep) && (action.getFinishFlag() == null
//					|| ((action.getFinishFlag() != null) && !action.getFinishFlag()))) {
				ActionResponse actionResponse = new ActionResponse();
				actionResponse.setAId(action.getAId());
				actionResponse.setStepName(action.getConfigStep().getStepName());
				actionResponse.setFormId(action.getForm().getFId());
				actionResponse.setComments(action.getComments());
				actionResponse.setSignoffType(action.getSignoffType());
				actionResponse.setType(action.getType());
				actionResponse.setUsername(action.getUser().getUsername());
				actionResponse.setFormNumber(action.getForm().getFormNumber());
				actionResponse.setFormTypeName(action.getForm().getConfigFormType().getName());
				actionResponse.setFormDescription(action.getForm().getDescription());
				actionResponse.setFinishFlag(action.getFinishFlag());
				results.add(actionResponse);
//			}
		}
		log.info("My actions size: {}" , results.size());
//		System.out.println("My actions size:" + results.size());
		return results;
	}

	public List<ActionResponse> listActionsByFormId(Long formId) {
//		System.out.println("userId:"+userId);
		List<ActionResponse> results = new ArrayList<ActionResponse>();
		List<Action> source = actionRepository.findByFormIdAndType(formId, "A");
		for (Action action : source) {
			ConfigStep actionStep = action.getConfigStep();
//			System.out.println("actionStep:"+actionStep);
			ConfigStep currStep = action.getForm().getCurrStep();
//			System.out.println("currStep:"+currStep);

			ActionResponse actionResponse = new ActionResponse();
			actionResponse.setAId(action.getAId());
			actionResponse.setStepName(action.getConfigStep().getStepName());
			actionResponse.setFormId(action.getForm().getFId());
			actionResponse.setComments(action.getComments());
			actionResponse.setSignoffType(action.getSignoffType());
			actionResponse.setType(action.getType());
			actionResponse.setUsername(action.getUser().getUsername());
			actionResponse.setFormNumber(action.getForm().getFormNumber());
			actionResponse.setFormTypeName(action.getForm().getConfigFormType().getName());
			actionResponse.setFormDescription(action.getForm().getDescription());
			actionResponse.setFinishFlag(action.getFinishFlag());
			results.add(actionResponse);

		}
		log.info("My actions size: {}" , results.size());
//		System.out.println("My actions size:" + results.size());
		return results;
	}
	
	@Transactional
	public List<ActionResponse> addApprovers(Long formId, List<String> approverIds) {
		ZAccount creator = new ZAccount();
		List<ActionResponse> addedApprovers = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new InternalAuthenticationServiceException("Can't get current user");
		}
		creator = (ZAccount) authentication.getPrincipal();
		
		Form form = formRepository.getReferenceById(formId);
		ConfigStep step = form.getCurrStep();
		
		for (String approverId: approverIds) {
			ZAccount newApprover = userRepository.getReferenceById(approverId);
			List<Action> userCurrActions = actionRepository.findByTypeAndFormAndConfigStepAndUserAndFinishFlag("A", form, step, newApprover, false);
			if (userCurrActions.size() == 0) {
				Action action = Action.builder()
						.noticeFlag(false)
						.canNoticeFlag(true)
						.finishFlag(false)
						.configStep(step)
						.form(form)
						.type("A")
						.user(newApprover)
						.creator(creator)
						.build();
				Action savedAction = actionRepository.save(action);
				addedApprovers.add(new ActionResponse(savedAction));
			}
			
		}		
		return addedApprovers;
	}
	
	@Transactional
	public ActionResponse returnToPending(Long formId) {
		ZAccount creator = new ZAccount();
//		List<ActionResponse> addedApprovers = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new InternalAuthenticationServiceException("Can't get current user");
		}
		creator = (ZAccount) authentication.getPrincipal();
		
		Form form = formRepository.getReferenceById(formId);
		ConfigStep step = form.getCurrStep();
		
//		for (String approverId: approverIds) {
//			ZAccount newApprover = userRepository.getReferenceById(approverId);
//			List<Action> userCurrActions = actionRepository.findByTypeAndFormAndConfigStepAndUserAndFinishFlag("A", form, step, newApprover, false);
//			if (userCurrActions.size() == 0) {
		Action action = Action.builder()
				.noticeFlag(false)
				.canNoticeFlag(true)
				.finishFlag(false)
				.configStep(step)
				.form(form)
				.type("R")
				.user(creator)
				.creator(creator)
				.build();
		Action savedAction = actionRepository.save(action);
//				addedApprovers.add(new ActionResponse(savedAction));
//			}
			
//		}		
		return new ActionResponse(savedAction);
	}
	
	@Transactional
	public void initStepActions(Form form, ConfigStep nextStep) {
//		ConfigWorkflow configWorkflow = form.getCWorkflow();
//		List<ConfigStep> steps = configWorkflow.getCSteps();
//			ConfigStep nextStep = form.getCurrStep().getNextStep();
//		for (int i = iStart; i < steps.size(); i++) {
//			ConfigStep step = steps.get(i);
//		steps.forEach((step)->{
			log.info("Step: {}" , nextStep.getStepName());
//			System.out.println("Step:" + step.getStepName());
			List<Object> allApproverIds = new LinkedList<>();
			List<Object> stepApproverUserIds = nextStep.getApprovers();
			if (stepApproverUserIds == null) stepApproverUserIds = new LinkedList<>();
			List<Object> criteriaApproverUserIds = queryService.getApproversByAllStepCriterias(form.getFId(), nextStep);
			
			allApproverIds = Stream.concat(stepApproverUserIds.stream(), criteriaApproverUserIds.stream())
            .distinct()
            .collect(Collectors.toList());
			
			if (allApproverIds != null) {
				allApproverIds.forEach((userId) -> {
//					Long lUserId = Long.valueOf(userId.toString());
					ZAccount creator = new ZAccount();
					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					if (authentication == null || !authentication.isAuthenticated()) {
						throw new InternalAuthenticationServiceException("Can't get current user");
					}
					log.info("ZAccount: {}", authentication.getPrincipal());
//					System.out.println(authentication.getPrincipal());
//					String userName = (String) authentication.getPrincipal();
//					Optional<ZAccount> oCreator = userRepository.findByUsername(userName);
//					creator = oCreator.get();
					creator = (ZAccount) authentication.getPrincipal();

					ZAccount user = userRepository.getReferenceById(userId.toString());
					log.info("Approve User: {}" , user);
//					System.out.println("Approve User:" + user);
					Action action = new Action();
					action.setConfigStep(nextStep);
					action.setForm(form);
					action.setType("A");
					action.setNoticeFlag(false);
					action.setUser(user);
					action.setCreator(creator);
					actionRepository.save(action);
				});
			}
			List<Object> notifyUserIds = nextStep.getNotifiers();
			if (notifyUserIds != null) {
				notifyUserIds.forEach((userId) -> {
//					Long lUserId = Long.valueOf(userId.toString());
					ZAccount creator = new ZAccount();
					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					if (authentication == null || !authentication.isAuthenticated()) {
						throw new InternalAuthenticationServiceException("Can't get current user");
					}
					log.info("ZAccount: {}", authentication.getPrincipal());
//					System.out.println(authentication.getPrincipal());
//					String userName = (String) authentication.getPrincipal();
//					Optional<ZAccount> oCreator = userRepository.findByUsername(userName);
//					creator = oCreator.get();
					creator = (ZAccount) authentication.getPrincipal();

					ZAccount user = userRepository.getReferenceById(userId.toString());
					
					log.info("Notify User: {}", user);
//					System.out.println("Notify User:" + user);
					Action action = new Action();
					action.setConfigStep(nextStep);
					action.setForm(form);
					action.setType("N");
					action.setNoticeFlag(false);
					action.setUser(user);
					action.setCreator(creator);
					actionRepository.save(action);
				});
			}
		}
	}
