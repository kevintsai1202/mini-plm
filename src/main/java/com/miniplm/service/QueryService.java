package com.miniplm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniplm.convert.FormConverter;
import com.miniplm.convert.FormDataConverter;
import com.miniplm.entity.ConfigCriteriaItem;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigStepCriteria;
import com.miniplm.entity.Form;
import com.miniplm.entity.FormData;
import com.miniplm.entity.OperatorEnum;
import com.miniplm.repository.ActionRepository;
import com.miniplm.repository.ConfigFormNumberRepository;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.repository.ConfigWorkflowRepository;
import com.miniplm.repository.FormDataRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QueryService {
	@Resource
	private FormRepository formRepository;
	
	@Resource
	private FormDataRepository formDataRepository;
	
	@Resource
	private UserRepository userRepository;
	
	@Resource
	private ConfigFormTypeRepository configFormTypeRepository;
	
	@Resource
	private ConfigWorkflowRepository configWorkflowRepository;
	
	@Resource
	private ConfigFormNumberRepository configFormNumberRepository;
	
	@Resource
	private ActionRepository actionRepository;
	
	@Autowired
	private ActionService actionService;
	
	private final List formFields = Arrays.asList("formNumber", "description");

	public List<Object> getNotifiersByAllStepCriterias(Long formId, ConfigStep step) {
		List<Object> allNotifiers = new LinkedList<>();
		List<ConfigStepCriteria> cStepCriterias = step.getCStepCriterias();
		for (ConfigStepCriteria cStepCriteria : cStepCriterias) {
			List<Object> notifiers = getNotifiersByStepCriteria(formId, cStepCriteria);
			if ((notifiers!=null) && notifiers.size() >0) {
				allNotifiers = Stream.concat(allNotifiers.stream(), notifiers.stream())
				                .distinct()
				                .collect(Collectors.toList());
			}
		}
		return allNotifiers;
	}
	
	public List<Object> getApproversByAllStepCriterias(Long formId, ConfigStep step) {
		List<Object> allApprovers = new LinkedList<>();
		List<ConfigStepCriteria> cStepCriterias = step.getCStepCriterias();
		for (ConfigStepCriteria cStepCriteria : cStepCriterias) {
			List<Object> approvers = getApproversByStepCriteria(formId, cStepCriteria);
			if ((approvers!=null) && approvers.size() >0) {
				allApprovers = Stream.concat(allApprovers.stream(), approvers.stream())
				                .distinct()
				                .collect(Collectors.toList());
			}
		}
		return allApprovers;
	}
	
	
	public List<Object> getApproversByStepCriteria(Long formId, ConfigStepCriteria stepCriteria){
		if (matchFormByCriteria(formId, stepCriteria.getCCriteriaNode())) {
			return stepCriteria.getApprovers();
		}else
			return new LinkedList<Object>(); 
	}
	
	public List<Object> getNotifiersByStepCriteria(Long formId, ConfigStepCriteria stepCriteria){
		if (matchFormByCriteria(formId, stepCriteria.getCCriteriaNode())) {
			return stepCriteria.getNotifiers();
		}else
			return new LinkedList<Object>(); 
	}
	
//	public Boolean matchFormByCriteria1(Long formId, ConfigCriteriaNode criteriaNode){
//		List<ConfigCriteriaItem> criteriaItems = criteriaNode.getCriteriaItems();
//		return formRepository.exists((SpecForm, query, cb) -> {
//		
////			List<Predicate> predicates = new ArrayList<>();
//            Join<Form, FormData> formData = SpecForm.join("formData");
//            Predicate predicate = cb.equal(SpecForm.get("fId"), formId);
//            predicate = cb.and(predicate, 
//				            		cb.or(
//				            				cb.equal(formData.get("select01"), "選項2"),
//				            				cb.equal(formData.get("select01"), "選項4")
//				            		      )
//		            		  );
//            
//            return predicate;
//		});
//	}
	
	public Boolean matchFormByCriteria(Long formId, ConfigCriteriaNode criteriaNode){
//		List formFields = Arrays.asList("formNumber", "description");
//		List<Form> forms = new ArrayList<>();
		List<ConfigCriteriaItem> criteriaItems = criteriaNode.getCriteriaItems();
        
        return formRepository.exists((specForm, query, cb) -> {
        	Join<Form, FormData> specFormData = specForm.join("formData");
        	Predicate formIdPredicate = cb.equal(specForm.get("fId"), formId); //表單id是初始條件
//        	List<Predicate> predicates = new ArrayList<>();
        	
        	if ((criteriaItems == null) || criteriaItems.size()==0) {
        		return formIdPredicate;
        	}
        	
        	Predicate otherPredicate = null;
    		for (ConfigCriteriaItem criteriaItem: criteriaItems) {
    			if (formFields.contains(criteriaItem.getField())){ //判斷是form還是formdata
	    			if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.equal( specForm.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.and(otherPredicate, cb.equal( specForm.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.equal( specForm.get(criteriaItem.getField()),criteriaItem.getValue()) :cb.or(otherPredicate, cb.equal( specForm.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
	    				}	  
//	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.like( specForm.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : cb.and(otherPredicate, cb.like( specForm.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.like( specForm.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : cb.or(otherPredicate, cb.like( specForm.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
	    				}	
//	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.NotLike)) {
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?cb.notLike( specForm.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : cb.and(otherPredicate, cb.notLike( specForm.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?cb.notLike( specForm.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : cb.or(otherPredicate, cb.notLike( specForm.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
        					break;
	    				}
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.NotEqualTo)) {
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?cb.notEqual( specForm.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.and(otherPredicate, cb.notEqual( specForm.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?cb.notEqual( specForm.get(criteriaItem.getField()),criteriaItem.getValue()) :cb.or(otherPredicate, cb.notEqual( specForm.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				}	  
//    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
    			}
    			else {
    				if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
    					switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.equal( specFormData.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.and(otherPredicate, cb.equal( specFormData.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.equal( specFormData.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.or(otherPredicate, cb.equal( specFormData.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
    					}	
//    					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.IsNotNull)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.isNotNull( specFormData.get(criteriaItem.getField())) : cb.and(otherPredicate , cb.isNotNull( specFormData.get(criteriaItem.getField())));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.isNotNull( specFormData.get(criteriaItem.getField())) : cb.or(otherPredicate, cb.isNotNull( specFormData.get(criteriaItem.getField())));
	        					break;
						}
//	    				predicates.add( cb.isNotNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.IsNull)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.isNull( specFormData.get(criteriaItem.getField())) : cb.and(otherPredicate, cb.isNull( specFormData.get(criteriaItem.getField())));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.isNull( specFormData.get(criteriaItem.getField())) : cb.or(otherPredicate, cb.isNull( specFormData.get(criteriaItem.getField())));
	        					break;
						}
//	    				predicates.add( cb.isNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.like( specFormData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :cb.and(otherPredicate, cb.like( specFormData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.like( specFormData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :cb.or(otherPredicate, cb.like( specFormData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
						}
//	    				predicates.add(  cb.equal( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.NotEqualTo)) {
    					switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?cb.notEqual( specFormData.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.and(otherPredicate, cb.notEqual( specFormData.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?cb.notEqual( specFormData.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.or(otherPredicate, cb.notEqual( specFormData.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.NotLike)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.notLike( specFormData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :cb.and(otherPredicate, cb.notLike( specFormData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.notLike( specFormData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :cb.or(otherPredicate, cb.notLike( specFormData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
						}
//	    				predicates.add(  cb.equal( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
    			}
    		}
            return cb.and(formIdPredicate, otherPredicate);
        });
	}

}
