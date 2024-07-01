package com.miniplm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.miniplm.convert.FormConverter;
import com.miniplm.convert.FormDataConverter;
import com.miniplm.entity.ConfigCriteriaItem;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigFormNumber;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.entity.Form;
import com.miniplm.entity.FormData;
import com.miniplm.entity.OperatorEnum;
import com.miniplm.entity.ZAccount;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.repository.ConfigWorkflowRepository;
import com.miniplm.repository.FormDataRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.repository.UserRepository;
import com.miniplm.request.FormDataRequest;
import com.miniplm.request.FormRequest;
import com.miniplm.response.FormResponse;
import com.miniplm.utils.MyReflectionUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FormDetailsService {
	@Resource
	FormRepository formRepository;
	
	@Resource
	FormDataRepository formDataRepository;
	
	@Resource
	UserRepository userRepository;
	
	@Resource
	ConfigFormTypeRepository configFormTypeRepository;
	
	@Resource
	ConfigWorkflowRepository configWorkflowRepository;
	
	@Resource
	ConfigFormNumberRepository configFormNumberRepository;
	
//	@Resource
//	ActionRepository actionRepository;
	
	@Autowired
	ActionService actionService;
	
	@Autowired
	private FormDataConverter formDataConverter;
	
	@Autowired
	private FormConverter formConverter;

	@Transactional
	public List<FormResponse> listMyForm(){
		ZAccount me = (ZAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("me: "+me);
		List<FormResponse> results= new ArrayList<FormResponse>();		
//		List<Form> forms = formRepository.findAll(Sort.by("createTime").descending());
		List<Form> forms = formRepository.findByCreator(me);
//		List<Form> forms = formRepository.findByCreatorId(me.getId());
		log.info("Form size: {}" , forms.size());
//		System.out.println("Form size:" + forms.size());
		for (Form form: forms) {
//			FormData fd = form.getFormData();
			FormResponse formDetails = null;
			
//			System.out.println("Form No:" + form.getFormNumber());
//			System.out.println("Form Type Name:" + form.getConfigFormType().getName());
//			System.out.println("Form Description:" + form.getDescription());
//			System.out.println("Form Data:" + form.getFormData());
			String workflowName = "";
			String stepName = "";
			if (form.getCWorkflow() != null) {
				
				workflowName = form.getCWorkflow().getName();
				stepName = form.getCurrStep().getStepName();
			}
			
			formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), null, workflowName, stepName, form.getCreator().getId(), form.getCreateTime());

//			System.out.println("Form detail:"+form.getFormData());
			results.add(formDetails);
		}
		
		
		return results;
	}
	
	@Transactional
	public Page<FormResponse> listMyForm(Pageable pageable){
		ZAccount me = (ZAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("me: "+me);
		List<FormResponse> results= new ArrayList<FormResponse>();		
//		List<Form> forms = formRepository.findAll(Sort.by("createTime").descending());
		Page<Form> forms = formRepository.findByCreator(me, pageable);
		List<FormResponse> formResponses = forms.getContent().stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
		
		return new PageImpl<>(formResponses, pageable, forms.getTotalElements());
//		List<Form> forms = formRepository.findByCreatorId(me.getId());
//		log.info("Form size: {}" , forms.size());
//		System.out.println("Form size:" + forms.size());
//		for (Form form: forms) {
////			FormData fd = form.getFormData();
//			FormResponse formDetails = null;
//			
////			System.out.println("Form No:" + form.getFormNumber());
////			System.out.println("Form Type Name:" + form.getConfigFormType().getName());
////			System.out.println("Form Description:" + form.getDescription());
////			System.out.println("Form Data:" + form.getFormData());
//			String workflowName = "";
//			String stepName = "";
//			if (form.getCWorkflow() != null) {
//				
//				workflowName = form.getCWorkflow().getName();
//				stepName = form.getCurrStep().getStepName();
//			}
//			
//			formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), null, workflowName, stepName, form.getCreator().getId(), form.getCreateTime());
//
////			System.out.println("Form detail:"+form.getFormData());
//			results.add(formDetails);
//		}
//		
//		
//		return results;
	}
	
	@Transactional
	public List<FormResponse> listAllForm(){
		List<FormResponse> results= new ArrayList<FormResponse>();		
		List<Form> forms = formRepository.findAll(Sort.by("createTime").descending());
		log.info("Form size: {}" , forms.size());
//		System.out.println("Form size:" + forms.size());
		for (Form form: forms) {
//			FormData fd = form.getFormData();
			FormResponse formDetails = null;
			
//			System.out.println("Form No:" + form.getFormNumber());
//			System.out.println("Form Type Name:" + form.getConfigFormType().getName());
//			System.out.println("Form Description:" + form.getDescription());
//			System.out.println("Form Data:" + form.getFormData());
			String workflowName = "";
			String stepName = "";
			if (form.getCWorkflow() != null) {
				
				workflowName = form.getCWorkflow().getName();
				stepName = form.getCurrStep().getStepName();
			}
			
			formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), null, workflowName, stepName, form.getCreator().getId(), form.getCreateTime());

//			System.out.println("Form detail:"+form.getFormData());
			results.add(formDetails);
		}
		return results;
	}
	
	@Transactional
	public Page<FormResponse> listAllForm(Pageable pageable){
//		List<FormResponse> results= new ArrayList<FormResponse>();		
		Page<Form> forms = formRepository.findAll(pageable);
		List<FormResponse> formResponses = forms.getContent().stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
		
//		log.info("Form page size: {}" , forms.getSize());
//		System.out.println("Form size:" + forms.size());
//		for (Form form: forms) {
//			FormData fd = form.getFormData();
//			FormResponse formDetails = null;
			
//			System.out.println("Form No:" + form.getFormNumber());
//			System.out.println("Form Type Name:" + form.getConfigFormType().getName());
//			System.out.println("Form Description:" + form.getDescription());
//			System.out.println("Form Data:" + form.getFormData());
//			String workflowName = "";
//			String stepName = "";
//			if (form.getCWorkflow() != null) {
//				
//				workflowName = form.getCWorkflow().getName();
//				stepName = form.getCurrStep().getStepName();
//			}
			
//			formDetails = new FormResponse(form);

//			System.out.println("Form detail:"+form.getFormData());
//			results.add(formDetails);
//		}
//		return results;
		return new PageImpl<>(formResponses, pageable, forms.getTotalElements());
	}
	
	@Transactional
	public List<FormResponse> listByFormTypeId(Long formTypeId){
		List<FormResponse> results= new ArrayList<FormResponse>();		
		ConfigFormType configFormType =  configFormTypeRepository.getReferenceById(formTypeId);
		
		List<Form> forms = formRepository.findByConfigFormType(configFormType);
		
		log.info("Form size: {}" , forms.size());
//		System.out.println("Form size:" + forms.size());
		for (Form form: forms) {
			FormResponse formDetails = null;

			String workflowName = "";
			String stepName = "";
			if (form.getCWorkflow() != null) {
				
				workflowName = form.getCWorkflow().getName();
				stepName = form.getCurrStep().getStepName();
			}
			
			formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), null, workflowName, stepName, form.getCreator().getId(), form.getCreateTime());

//			System.out.println("Form detail:"+form.getFormData());
			results.add(formDetails);
		}
		return results;
	}
	
	@Transactional
	public Page<FormResponse> listByFormTypeId(Long formTypeId, Pageable pageable){
		List<FormResponse> results= new ArrayList<FormResponse>();		
		ConfigFormType configFormType =  configFormTypeRepository.getReferenceById(formTypeId);
		
		Page<Form> forms = formRepository.findByConfigFormType(configFormType, pageable);
		List<FormResponse> formResponses = forms.getContent().stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
//		log.info("Form size: {}" , forms.size());
//		System.out.println("Form size:" + forms.size());
//		for (Form form: forms) {
//			FormResponse formDetails = null;
//
//			String workflowName = "";
//			String stepName = "";
//			if (form.getCWorkflow() != null) {
//				
//				workflowName = form.getCWorkflow().getName();
//				stepName = form.getCurrStep().getStepName();
//			}
//			
//			formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), null, workflowName, stepName, form.getCreator().getId(), form.getCreateTime());
//
////			System.out.println("Form detail:"+form.getFormData());
//			results.add(formDetails);
//		}
		return new PageImpl<>(formResponses, pageable, forms.getTotalElements());
	}
	
	public FormResponse getFormDetailsByFormNumber(String FormNumber) {
		Form form = formRepository.findByFormNumber(FormNumber);
		FormResponse formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), form.getFormData(), form.getCWorkflow().getName(), form.getCurrStep().getStepName(), form.getCreator().getId(), form.getCreateTime());
		return formDetails;
	}
	
	@Transactional
	public FormData updateFormData(Long id, FormDataRequest formDataReq) {
//		boolean initActions = false;
		Form form = formRepository.getReferenceById(id);
		form.setFormNumber(formDataReq.getFormNumber());
		form.setDescription(formDataReq.getDescription());
		ConfigWorkflow cWorkflow = configWorkflowRepository.getReferenceById(formDataReq.getWorkflow());
//		if ((form.getCWorkflow() == null) && (formDataReq.getWorkflow() != null)) {
//			initActions = true;
//		}
		form.setCWorkflow(cWorkflow);
		Form updatedForm = formRepository.save(form);
//		FormData formData = form.getFormData();
		FormData formData = formDataConverter.requestToEntity(formDataReq);
		formData.setForm(updatedForm);
		if(updatedForm.getFormData() != null)
			formData.setFdId(updatedForm.getFormData().getFdId());
		FormData updatedformData = formDataRepository.save(formData);
//		if (initActions)
//			actionService.initActions(updatedForm);
		return updatedformData;
	}
	
	@Transactional
	public FormData patchUpdateFormData(String formNumber, FormDataRequest formDataReq) {
		Form form = formRepository.findByFormNumber(formNumber);
		FormData formData = form.getFormData();
		
		MyReflectionUtils.mapNonNullFields(formDataReq, formData);
		
		
//		formData.setForm(updatedForm);
//		if(updatedForm.getFormData() != null)
//			formData.setFdId(updatedForm.getFormData().getFdId());
		FormData updatedformData = formDataRepository.save(formData);
//		if (initActions)
//			actionService.initActions(updatedForm);
		return updatedformData;
	}
	
	@Transactional
	public Form createForm(Long formTypeId, FormRequest formReq) {
		Form form = formConverter.requestToEntity(formReq);
		FormData fromData = new FormData();
//		ZAccount creator = new ZAccount();
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication == null || !authentication.isAuthenticated()) {
//            throw new InternalAuthenticationServiceException("Can get current user");
//        }
//        log.info("ZAccount: {}", authentication.getPrincipal());
//        System.out.println(authentication.getPrincipal());
//        creator = (ZAccount) authentication.getPrincipal();
//        Optional<ZAccount> oCreator = userRepository.findByUsername(userName);
//       	creator = oCreator.get();
		
		fromData = formDataRepository.save(fromData);
		ConfigFormType formType = configFormTypeRepository.getReferenceById(formTypeId);
		ConfigFormNumber configFormNumber = configFormNumberRepository.getReferenceById(formReq.getConfigFormNumberId());
		ConfigWorkflow configWorkflow = formType.getConfigWorkflow();
		ConfigStep currStep = configWorkflow.getCSteps().get(0);
		form.setConfigFormType(formType);
		String formNumber = configFormNumber.generateFormNumber();
//		configFormNumber.setCurrSeq(configFormNumber.getCurrSeq()+1);
		configFormNumberRepository.save(configFormNumber);//更新seq
		log.info("Form Number: {}",formNumber);
//		System.out.println("Form Number:"+formNumber);
		form.setFormNumber(formNumber);
		form.setFormData(fromData);
		form.setCurrStep(currStep);
		form.setCWorkflow(configWorkflow);
//		form.setCreator(creator);
		form = formRepository.save(form);
//		actionService.initActions(form);
		return form;
	}
	
	public List<Form> findFormByCriteria(ConfigCriteriaNode criteriaNode){
		List formFields = Arrays.asList("formNumber", "description");
		List<Form> forms = new ArrayList<>();
		List<ConfigCriteriaItem> criteriaItems = criteriaNode.getCriteriaItems();
//		forms = formRepository.findAll()
//		Specification<Form> sf = 
//		(form, query, cb) -> {
//            Join<Form, FormData> formData = form.join("formData");
//            List<Predicate> predicates = new ArrayList<>();
//    		for (ConfigCriteriaItem criteriaItem: criteriaItems) {
//    			if (formFields.contains(criteriaItem.getField())){
//	    			if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
//	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
//	    			}
//    			}
//    			else {
//    				if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
//	    				predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
//	    			}
//    			}
//    		}
//            return cb.and((Predicate[]) predicates.toArray());
//        };
        
        forms = formRepository.findAll((form, query, cb) -> {
        	if ((criteriaItems == null) || criteriaItems.size()==0) {
        		return cb.conjunction();
        	}
            Join<Form, FormData> formData = form.join("formData");
            List<Predicate> predicates = new ArrayList<>();
    		for (ConfigCriteriaItem criteriaItem: criteriaItems) {
    			if (formFields.contains(criteriaItem.getField())){
	    			if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
    			}
    			else {
    				if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
	    				predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.IsNotNull)) {
	    				predicates.add( cb.isNotNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.IsNull)) {
	    				predicates.add( cb.isNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				predicates.add(  cb.equal( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
    			}
    		}
            return cb.and(predicates.toArray(new Predicate[0]));
        });

		return forms;
	}
	
	public ConfigStep getFirstStep(Form form) {
		return form.getCWorkflow().getCSteps().get(0);
	}

//	public void initActions(Form form, int iStart) {
//		ConfigWorkflow configWorkflow = form.getCWorkflow();
//		List<ConfigStep> steps = configWorkflow.getCSteps();
//		
//		for (int i = iStart ; i < steps.size() ; i++) {
//			ConfigStep step = steps.get(i);
////		steps.forEach((step)->{
//			System.out.println("Step:"+step.getStepName());
//			List<Object> approverUserIds= step.getApprovers();
//			if (approverUserIds !=null) {
//				approverUserIds.forEach((userId)->{ 
////					Long lUserId = Long.valueOf(userId.toString());
//					ZAccount creator = new ZAccount();
//					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			        if(authentication == null || !authentication.isAuthenticated()) {
//			            throw new InternalAuthenticationServiceException("Can't get current user");
//			        }
//			        System.out.println(authentication.getPrincipal());
//			        String userName = (String) authentication.getPrincipal();
//			        Optional<ZAccount> oCreator = userRepository.findByUsername(userName);
//			       	creator = oCreator.get();
//					
//					ZAccount user = userRepository.getReferenceById(userId.toString());
//					System.out.println(" Approve User:"+ user);
//					Action action = new Action();
//					action.setConfigStep(step);
//					action.setForm(form);
//					action.setType("A");
//					action.setNoticeFlag(false);
//					action.setUser(user);
//					action.setCreator(creator);
//					actionRepository.save(action);
//				});
//			}
//			List<Object> notifyUserIds= step.getNotifiers();
//			if (notifyUserIds!=null) {
//				notifyUserIds.forEach((userId)->{ 
////					Long lUserId = Long.valueOf(userId.toString());
//					ZAccount creator = new ZAccount();
//					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			        if(authentication == null || !authentication.isAuthenticated()) {
//			            throw new InternalAuthenticationServiceException("Can get current user");
//			        }
//			        System.out.println(authentication.getPrincipal());
//			        String userName = (String) authentication.getPrincipal();
//			        Optional<ZAccount> oCreator = userRepository.findByUsername(userName);
//			       	creator = oCreator.get();
//					
//					ZAccount user = userRepository.getReferenceById(userId.toString());
//					System.out.println(" Notify User:"+ user);
//					Action action = new Action();
//					action.setConfigStep(step);
//					action.setForm(form);
//					action.setType("N");
//					action.setNoticeFlag(false);
//					action.setUser(user);
//					action.setCreator(creator);
//					actionRepository.save(action);
//				});
//			}
//		};
//	}
}
