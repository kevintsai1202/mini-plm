package com.miniplm.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.miniplm.convert.FormConverter;
import com.miniplm.convert.FormDataConverter;
import com.miniplm.entity.ChangeTypeEnum;
import com.miniplm.entity.ConfigCriteriaItem;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigFormNumber;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.entity.Form;
import com.miniplm.entity.FormData;
import com.miniplm.entity.FormHistory;
import com.miniplm.entity.OperatorEnum;
import com.miniplm.entity.ZAccount;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.repository.ConfigWorkflowRepository;
import com.miniplm.repository.FormDataRepository;
import com.miniplm.repository.FormHistoryRepository;
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
	
	@Resource
	FormHistoryRepository formHistoryRepository;
	
	@Autowired
	ActionService actionService;
	
	@Autowired
	private FormDataConverter formDataConverter;
	
	@Autowired
	private FormConverter formConverter;

	public List<FormResponse> listMyForm(){
		ZAccount me = (ZAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("me: "+me);
		List<FormResponse> results= new ArrayList<FormResponse>();		
		List<Form> forms = formRepository.findByCreator(me);
		log.info("Form size: {}" , forms.size());
		for (Form form: forms) {
			FormResponse formDetails = null;
			String workflowName = "";
			String stepName = "";
			if (form.getCWorkflow() != null) {
				
				workflowName = form.getCWorkflow().getName();
				stepName = form.getCurrStep().getStepName();
			}
			
			formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), null, workflowName, stepName, form.getCreator().getId(), form.getCreateTime());
			results.add(formDetails);
		}		
		return results;
	}
	
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
	}
	
	public List<FormResponse> listAllForm(){
		List<FormResponse> results= new ArrayList<FormResponse>();		
		List<Form> forms = formRepository.findAll(Sort.by("createTime").descending());
		log.info("Form size: {}" , forms.size());
		for (Form form: forms) {
			FormResponse formDetails = null;
			String workflowName = "";
			String stepName = "";
			if (form.getCWorkflow() != null) {
				
				workflowName = form.getCWorkflow().getName();
				stepName = form.getCurrStep().getStepName();
			}
			
			formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), null, workflowName, stepName, form.getCreator().getId(), form.getCreateTime());
			results.add(formDetails);
		}
		return results;
	}
	
	public Page<FormResponse> listAllForm(Pageable pageable){
		Page<Form> forms = formRepository.findAll(pageable);
		List<FormResponse> formResponses = forms.getContent().stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
		return new PageImpl<>(formResponses, pageable, forms.getTotalElements());
	}
	
	public List<FormResponse> listByFormTypeId(Long formTypeId){
		List<FormResponse> results= new ArrayList<FormResponse>();		
		ConfigFormType configFormType =  configFormTypeRepository.getReferenceById(formTypeId);
		
		List<Form> forms = formRepository.findByConfigFormType(configFormType);
		
		log.info("Form size: {}" , forms.size());
		for (Form form: forms) {
			FormResponse formDetails = null;

			String workflowName = "";
			String stepName = "";
			if (form.getCWorkflow() != null) {
				
				workflowName = form.getCWorkflow().getName();
				stepName = form.getCurrStep().getStepName();
			}
			
			formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), null, workflowName, stepName, form.getCreator().getId(), form.getCreateTime());
			results.add(formDetails);
		}
		return results;
	}
	
	public Page<FormResponse> quickSearch(String keyword, Pageable pageable){
		Page<Form> forms = formRepository.findByFormNumberContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
		List<FormResponse> formResponses = forms.getContent().stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
		return new PageImpl<>(formResponses, pageable, forms.getTotalElements());
	}
	
	public Page<FormResponse> listByFormTypeId(Long formTypeId, Pageable pageable){
		List<FormResponse> results= new ArrayList<FormResponse>();		
		ConfigFormType configFormType =  configFormTypeRepository.getReferenceById(formTypeId);
		
		Page<Form> forms = formRepository.findByConfigFormType(configFormType, pageable);
		List<FormResponse> formResponses = forms.getContent().stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
		return new PageImpl<>(formResponses, pageable, forms.getTotalElements());
	}
	
	public FormResponse getFormDetailsByFormNumber(String FormNumber) {
		Form form = formRepository.findByFormNumber(FormNumber);
		FormResponse formDetails = new FormResponse(form.getFId(), form.getFormNumber(), form.getConfigFormType().getCfId(), form.getConfigFormType().getName(), form.getDescription(), form.getFormData(), form.getCWorkflow().getName(), form.getCurrStep().getStepName(), form.getCreator().getId(), form.getCreateTime());
		return formDetails;
	}
	
	@Transactional
    public void postUpdate(Form form) {
		Map<String, Object[]> differences = new HashMap<>();
        
        BeanWrapper oldBean = new BeanWrapperImpl(form.getOldData());
        BeanWrapper newBean = new BeanWrapperImpl(form);
        PropertyDescriptor properties[] = BeanUtils.getPropertyDescriptors(Form.class);
        
        for (PropertyDescriptor property : properties) {
        	String propertyName = property.getName();
            
        	Object value1 = oldBean.getPropertyValue(propertyName);
            Object value2 = newBean.getPropertyValue(propertyName);

            if ((value1 != null && !value1.equals(value2)) || (value1 == null && value2 != null)) {
                List<String> excepts = new ArrayList<>();
                excepts.add("oldData");
                excepts.add("formData");
                excepts.add("actions");
                excepts.add("updateTime");
                excepts.add("currStep");
                if (!excepts.contains(propertyName)) {
                	differences.put(propertyName, new Object[]{value1, value2});
                }
            }
        }
        
        if (differences.size()>0) {
        	StringBuffer sb = new StringBuffer();
        	differences.forEach((field, values) -> {
        		sb.append(field + ": " + values[0] + " -> " + values[1] + " ");
            });
        	log.info("Modify details:{}", sb.toString());
        	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        	ZAccount user = null;
    		if (authentication != null && authentication.isAuthenticated()) {
    			user = (ZAccount) authentication.getPrincipal();
    		}
    		   	
        	FormHistory history = FormHistory.builder()
        			.detail(sb.toString())
        			.form(form)
        			.accountName(user.getUsername())
        			.stepName(form.getCurrStep().getStepName())
        			.type(ChangeTypeEnum.Modify)
        			.build();

        	formHistoryRepository.save(history);
        }
    }
    
    @Transactional
    public void postUpdate(FormData formData) {
		Map<String, Object[]> differences = new HashMap<>();
        
        BeanWrapper oldBean = new BeanWrapperImpl(formData.getOldData());
        BeanWrapper newBean = new BeanWrapperImpl(formData);
        PropertyDescriptor properties[] = BeanUtils.getPropertyDescriptors(FormData.class);
        
        for (PropertyDescriptor property : properties) {
        	String propertyName = property.getName();
            
        	Object value1 = oldBean.getPropertyValue(propertyName);
            Object value2 = newBean.getPropertyValue(propertyName);

            if ((value1 != null && !value1.equals(value2)) || (value1 == null && value2 != null)) {
                List<String> excepts = new ArrayList<>();
                excepts.add("oldData");
                excepts.add("form");
                excepts.add("updateTime");
                if (!excepts.contains(propertyName)) {
                	differences.put(propertyName, new Object[]{value1, value2});
                }
            }
        }
        
        if (differences.size()>0) {
        	StringBuffer sb = new StringBuffer();
        	differences.forEach((field, values) -> {
        		sb.append(field + ": " + values[0] + " -> " + values[1] + " ");
            });
        	log.info("Modify details:{}", sb.toString());
        	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        	ZAccount user = null;
    		if (authentication != null && authentication.isAuthenticated()) {
    			user = (ZAccount) authentication.getPrincipal();
    		}
    		Form form = formData.getForm();
        	
        	FormHistory history = FormHistory.builder()
        			.detail(sb.toString())
        			.form(form)
        			.accountName(user.getUsername())
        			.stepName(form.getCurrStep().getStepName())
        			.type(ChangeTypeEnum.Modify)
        			.build();
        	
//        	applicationEventPublisher.publishEvent(new FormUpdateEvent(history));
        	formHistoryRepository.save(history);
        }
    }
	
	@Transactional
	public FormData updateFormData(Long id, FormDataRequest formDataReq) {

		Form form = formRepository.getReferenceById(id);
		form.setFormNumber(formDataReq.getFormNumber());
		form.setDescription(formDataReq.getDescription());
		ConfigWorkflow cWorkflow = configWorkflowRepository.getReferenceById(formDataReq.getWorkflow());

		form.setCWorkflow(cWorkflow);
		Form updatedForm = formRepository.save(form);
		postUpdate(updatedForm);

		FormData formData = formDataConverter.requestToEntity(formDataReq);
		formData.setForm(updatedForm);
		if(updatedForm.getFormData() != null)
			formData.setFdId(updatedForm.getFormData().getFdId());
		FormData updatedformData = formDataRepository.save(formData);
		
		postUpdate(updatedformData);

		return updatedformData;
	}
	
	@Transactional
	public FormData patchUpdateFormData(String formNumber, FormDataRequest formDataReq) {
		Form form = formRepository.findByFormNumber(formNumber);
		FormData formData = form.getFormData();
		
		MyReflectionUtils.mapNonNullFields(formDataReq, formData);

		FormData updatedformData = formDataRepository.save(formData);

		return updatedformData;
	}
	
	@Transactional
	public Form createForm(Long formTypeId, FormRequest formReq) {
		Form form = formConverter.requestToEntity(formReq);
		FormData fromData = new FormData();
		
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
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	ZAccount user = null;
		if (authentication != null && authentication.isAuthenticated()) {
			user = (ZAccount) authentication.getPrincipal();
		}
		
		FormHistory history = FormHistory.builder()
    			.detail("Create form: "+formNumber)
    			.form(form)
    			.accountName(user.getUsername())
    			.stepName(currStep.getStepName())
    			.type(ChangeTypeEnum.Create)
    			.build();
    	formHistoryRepository.save(history);
		
		return form;
	}
	
	public List<Form> findFormByCriteria(ConfigCriteriaNode criteriaNode){
		List formFields = Arrays.asList("formNumber", "description");
		List<Form> forms = new ArrayList<>();
		List<ConfigCriteriaItem> criteriaItems = criteriaNode.getCriteriaItems();
        
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
}
