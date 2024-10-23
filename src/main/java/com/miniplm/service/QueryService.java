package com.miniplm.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.miniplm.entity.ConfigCriteriaItem;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.ConfigListItem;
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
//@RequiredArgsConstructor
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
	
	@PersistenceContext
    private EntityManager entityManager;
	
//	@Autowired
//	private ActionService actionService;
	
	private final List<String> formFields = Arrays.asList("formNumber", "description");

	
	public List<ConfigListItem> getListItemsWithNativeQuery(String sql) {
//        String sql = "SELECT * FROM items WHERE 1=1";
        // 創建原生查詢
        Query query = entityManager.createNativeQuery(sql);
        List<ConfigListItem> itemResults = new ArrayList();
        List<Object[]> results = query.getResultList();
        int order = 1;
        for (Object[] row : results) {
        	ConfigListItem item = new ConfigListItem();
        	log.info("row:{}",row);
//            item.setCliId(((Number) row[0]).longValue());   // 轉換 ID
            item.setKey((String) row[0]);               // 設定名稱
            item.setValue((String) row[1]);             // 設定狀態
            item.setOrderBy(order++);
            itemResults.add(item);
        }
        
        // 執行查詢
        return itemResults;
    }
	
	public List<String> getNotifiersByAllStepCriterias(Long formId, ConfigStep step) {
		List<String> allNotifiers = new LinkedList<>();
		List<ConfigStepCriteria> cStepCriterias = step.getCStepCriterias();
		for (ConfigStepCriteria cStepCriteria : cStepCriterias) {
			List<String> notifiers = getNotifiersByStepCriteria(formId, cStepCriteria);
			if ((notifiers!=null) && notifiers.size() >0) {
				allNotifiers = Stream.concat(allNotifiers.stream(), notifiers.stream())
				                .distinct()
				                .collect(Collectors.toList());
			}
		}
		return allNotifiers;
	}
	
	public List<String> getApproversByAllStepCriterias(Long formId, ConfigStep step) {
		List<String> allApprovers = new LinkedList<>();
		List<ConfigStepCriteria> cStepCriterias = step.getCStepCriterias();
		for (ConfigStepCriteria cStepCriteria : cStepCriterias) {
			List<String> approvers = getApproversByStepCriteria(formId, cStepCriteria);
			if ((approvers!=null) && approvers.size() >0) {
				allApprovers = Stream.concat(allApprovers.stream(), approvers.stream())
				                .distinct()
				                .collect(Collectors.toList());
			}
		}
		return allApprovers;
	}
	
	
	public List<String> getApproversByStepCriteria(Long formId, ConfigStepCriteria stepCriteria){
		if (matchFormByCriteria(formId, stepCriteria.getCCriteriaNode())) {
			log.info("match: {}", stepCriteria);
			return stepCriteria.getApprovers();
		}else
			log.info("not match: {}", stepCriteria);
			return new LinkedList<String>(); 
	}
	
	public List<String> getNotifiersByStepCriteria(Long formId, ConfigStepCriteria stepCriteria){
		if (matchFormByCriteria(formId, stepCriteria.getCCriteriaNode())) {
			return stepCriteria.getNotifiers();
		}else
			return new LinkedList<String>(); 
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
        log.info("criteriaItems:{}", criteriaItems);
        return formRepository.exists((form, query, cb) -> {
        	Join<Form, FormData> formData = form.join("formData");
        	Predicate formIdPredicate = cb.equal(form.get("fId"), formId); //表單id是初始條件
//        	List<Predicate> predicates = new ArrayList<>();
        	
        	if ((criteriaItems == null) || criteriaItems.size()==0) {
        		return formIdPredicate;
        	}
        	
        	Predicate otherPredicate = null;
    		for (ConfigCriteriaItem criteriaItem: criteriaItems) {
    			log.info("criteriaItem:{}", criteriaItem.getValue());
    			if (formFields.contains(criteriaItem.getField())){ //判斷是form還是formdata
	    			if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.and(otherPredicate, cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.equal( form.get(criteriaItem.getField()),criteriaItem.getValue()) :cb.or(otherPredicate, cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
	    				}	  
//	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : cb.and(otherPredicate, cb.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : cb.or(otherPredicate, cb.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
	    				}	
//	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.NotLike)) {
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?cb.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : cb.and(otherPredicate, cb.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?cb.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : cb.or(otherPredicate, cb.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
        					break;
	    				}
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.NotEqualTo)) {
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?cb.notEqual( form.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.and(otherPredicate, cb.notEqual( form.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?cb.notEqual( form.get(criteriaItem.getField()),criteriaItem.getValue()) :cb.or(otherPredicate, cb.notEqual( form.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				}	  
//    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
    			}
    			else {
    				if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
    					switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.and(otherPredicate, cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.or(otherPredicate, cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
    					}	
//    					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.IsNotNull)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.isNotNull( formData.get(criteriaItem.getField())) : cb.and(otherPredicate , cb.isNotNull( formData.get(criteriaItem.getField())));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.isNotNull( formData.get(criteriaItem.getField())) : cb.or(otherPredicate, cb.isNotNull( formData.get(criteriaItem.getField())));
	        					break;
						}
//	    				predicates.add( cb.isNotNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.IsNull)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.isNull( formData.get(criteriaItem.getField())) : cb.and(otherPredicate, cb.isNull( formData.get(criteriaItem.getField())));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.isNull( formData.get(criteriaItem.getField())) : cb.or(otherPredicate, cb.isNull( formData.get(criteriaItem.getField())));
	        					break;
						}
//	    				predicates.add( cb.isNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.like( formData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :cb.and(otherPredicate, cb.like( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.like( formData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :cb.or(otherPredicate, cb.like( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
						}
//	    				predicates.add(  cb.equal( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.NotEqualTo)) {
    					switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?cb.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.and(otherPredicate, cb.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?cb.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()) : cb.or(otherPredicate, cb.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.In)) {
    					switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?cb.in( formData.get(criteriaItem.getField())).value(criteriaItem.getValue()) : cb.and(otherPredicate, cb.in( formData.get(criteriaItem.getField())).value(criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?cb.in( formData.get(criteriaItem.getField())).value(criteriaItem.getValue()) : cb.or(otherPredicate, cb.in( formData.get(criteriaItem.getField())).value(criteriaItem.getValue()));
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.Between)) {    				
	    				String betweenValues = criteriaItem.getValue();
	    				log.info("betweenValues:{}", betweenValues);
	    				String[] betweenValuesArr = betweenValues.split(",");
	    				Predicate betweenPredicate;
	    				if (criteriaItem.getField().startsWith("date")) {
	    					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    					Expression<Date> betweenField = formData.get(criteriaItem.getField());
	   
	    					try {
								betweenPredicate = cb.between(betweenField, format.parse(betweenValuesArr[0]), format.parse(betweenValuesArr[1]));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								betweenPredicate = cb.between(betweenField, new Date(), new Date());
							}
	    				}else {
	    					Expression<Double> betweenField = formData.get(criteriaItem.getField());
	    					betweenPredicate = cb.between(betweenField, Double.valueOf(betweenValuesArr[0]), Double.valueOf(betweenValuesArr[1]));
	    				}

	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)? betweenPredicate :cb.and(otherPredicate, betweenPredicate);
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)? betweenPredicate :cb.or(otherPredicate, betweenPredicate);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.ContantsAny)) {
	    				Expression<String> multiValues = formData.get(criteriaItem.getField()).as(String.class);
	    				
	    				String contantValues = criteriaItem.getValue();
	    				contantValues = StringUtils.replace(contantValues, "[", "");
	    				contantValues = StringUtils.replace(contantValues, "]", ""); 
	    				String[] contantValuesArr = contantValues.split(",");
	    				List<Predicate> contantPredicates = new ArrayList<Predicate>();
	    				
	    				for (String contantValue : contantValuesArr) {
	    					contantPredicates.add(cb.like(multiValues, "%"+contantValue+"%")); 	
	    				}
	    				
	    				Predicate[] contantPredicateArr = contantPredicates.toArray(new Predicate[0]);
	    				
	    				Predicate contantsAnyPredicates =  cb.or(contantPredicateArr);
	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)? contantsAnyPredicates : cb.and(otherPredicate, contantsAnyPredicates);
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)? contantsAnyPredicates : cb.or(otherPredicate, contantsAnyPredicates);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.ContantsAll)) {
	    				Expression<String> multiValues = formData.get(criteriaItem.getField()).as(String.class);
	    				
	    				String contantValues = criteriaItem.getValue();
	    				log.info("contantValues:{}", contantValues);
	    				contantValues = StringUtils.replace(contantValues, "[", "");
	    				contantValues = StringUtils.replace(contantValues, "]", ""); 
	    				String[] contantValuesArr = contantValues.split(",");
	    				List<Predicate> contantPredicates = new ArrayList<Predicate>();
	    				
	    				for (String contantValue : contantValuesArr) {
	    					contantPredicates.add(cb.like(multiValues, "%"+contantValue+"%")); 	
	    				}
	    				
	    				Predicate contantsAllPredicates =  cb.and(contantPredicates.toArray(new Predicate[0]));
	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?contantsAllPredicates :cb.and(otherPredicate, contantsAllPredicates);
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?contantsAllPredicates :cb.or(otherPredicate, contantsAllPredicates);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.NotLike)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?cb.notLike( formData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :cb.and(otherPredicate, cb.notLike( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?cb.notLike( formData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :cb.or(otherPredicate, cb.notLike( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
						}
//	    				predicates.add(  cb.equal( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
    			}
    		}
            return cb.and(formIdPredicate, otherPredicate);
//    		return formIdPredicate;
        });
	}
	
	public List<Form> queryByCriteria(ConfigCriteriaNode criteriaNode){
//		List formFields = Arrays.asList("formNumber", "description");
//		List<Form> forms = new ArrayList<>();
		List<ConfigCriteriaItem> criteriaItems = criteriaNode.getCriteriaItems();
        
        return formRepository.findAll((form, query, criteriaBuilder) -> {
        	Join<Form, FormData> formData = form.join("formData");
//        	Predicate formIdPredicate = cb.equal(specForm.get("fId"), formId); //表單id是初始條件
//        	List<Predicate> predicates = new ArrayList<>();
        	
//        	if ((criteriaItems == null) || criteriaItems.size()==0) {
//        		return formIdPredicate;
//        	}
        	
        	Predicate otherPredicate = null;
    		for (ConfigCriteriaItem criteriaItem: criteriaItems) {
    			if (formFields.contains(criteriaItem.getField())){ //判斷是form還是formdata
	    			if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()) : criteriaBuilder.and(otherPredicate, criteriaBuilder.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.equal( form.get(criteriaItem.getField()),criteriaItem.getValue()) :criteriaBuilder.or(otherPredicate, criteriaBuilder.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
	    				}	  
//	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : criteriaBuilder.and(otherPredicate, criteriaBuilder.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : criteriaBuilder.or(otherPredicate, criteriaBuilder.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
	    				}	
//	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.NotLike)) {
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?criteriaBuilder.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : criteriaBuilder.and(otherPredicate, criteriaBuilder.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?criteriaBuilder.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%") : criteriaBuilder.or(otherPredicate, criteriaBuilder.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
        					break;
	    				}
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.NotEqualTo)) {
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?criteriaBuilder.notEqual( form.get(criteriaItem.getField()), criteriaItem.getValue()) : criteriaBuilder.and(otherPredicate, criteriaBuilder.notEqual( form.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?criteriaBuilder.notEqual( form.get(criteriaItem.getField()),criteriaItem.getValue()) :criteriaBuilder.or(otherPredicate, criteriaBuilder.notEqual( form.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				}	  
//    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
    			}
    			else {
    				if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
    					switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()) : criteriaBuilder.and(otherPredicate, criteriaBuilder.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()) : criteriaBuilder.or(otherPredicate, criteriaBuilder.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
    					}	
//    					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.IsNotNull)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.isNotNull( formData.get(criteriaItem.getField())) : criteriaBuilder.and(otherPredicate , criteriaBuilder.isNotNull( formData.get(criteriaItem.getField())));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.isNotNull( formData.get(criteriaItem.getField())) : criteriaBuilder.or(otherPredicate, criteriaBuilder.isNotNull( formData.get(criteriaItem.getField())));
	        					break;
						}
//	    				predicates.add( cb.isNotNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.IsNull)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.isNull( formData.get(criteriaItem.getField())) : criteriaBuilder.and(otherPredicate, criteriaBuilder.isNull( formData.get(criteriaItem.getField())));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.isNull( formData.get(criteriaItem.getField())) : criteriaBuilder.or(otherPredicate, criteriaBuilder.isNull( formData.get(criteriaItem.getField())));
	        					break;
						}
//	    				predicates.add( cb.isNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.like( formData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :criteriaBuilder.and(otherPredicate, criteriaBuilder.like( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.like( formData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :criteriaBuilder.or(otherPredicate, criteriaBuilder.like( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
						}
//	    				predicates.add(  cb.equal( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.NotEqualTo)) {
    					switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?criteriaBuilder.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()) : criteriaBuilder.and(otherPredicate, criteriaBuilder.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?criteriaBuilder.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()) : criteriaBuilder.or(otherPredicate, criteriaBuilder.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.In)) {
    					switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = (otherPredicate == null)?criteriaBuilder.in( formData.get(criteriaItem.getField())).value(criteriaItem.getValue()) : criteriaBuilder.and(otherPredicate, criteriaBuilder.in( formData.get(criteriaItem.getField())).value(criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = (otherPredicate == null)?criteriaBuilder.in( formData.get(criteriaItem.getField())).value(criteriaItem.getValue()) : criteriaBuilder.or(otherPredicate, criteriaBuilder.in( formData.get(criteriaItem.getField())).value(criteriaItem.getValue()));
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.Between)) {    				
	    				String betweenValues = criteriaItem.getValue();
	    				log.info("betweenValues:{}", betweenValues);
	    				String[] betweenValuesArr = betweenValues.split(",");
	    				Predicate betweenPredicate;
	    				if (criteriaItem.getField().startsWith("date")) {
	    					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    					Expression<Date> betweenField = formData.get(criteriaItem.getField());
	   
	    					try {
								betweenPredicate = criteriaBuilder.between(betweenField, format.parse(betweenValuesArr[0]), format.parse(betweenValuesArr[1]));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								betweenPredicate = criteriaBuilder.between(betweenField, new Date(), new Date());
							}
	    				}else {
	    					Expression<Double> betweenField = formData.get(criteriaItem.getField());
	    					betweenPredicate = criteriaBuilder.between(betweenField, Double.valueOf(betweenValuesArr[0]), Double.valueOf(betweenValuesArr[1]));
	    				}

	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, betweenPredicate);
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, betweenPredicate);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.ContantsAny)) {
	    				Expression<String> multiValues = formData.get(criteriaItem.getField()).as(String.class);
	    				
	    				String contantValues = criteriaItem.getValue();
	    				log.info("contantValues:{}", contantValues);
	    				String[] contantValuesArr = contantValues.split(",");
	    				List<Predicate> contantPredicates = new ArrayList<Predicate>();
	    				
	    				for (String contantValue : contantValuesArr) {
	    					contantPredicates.add(criteriaBuilder.like(multiValues, "%"+contantValue+"%")); 	
	    				}
	    				
	    				Predicate contantsAnyPredicates =  criteriaBuilder.or(contantPredicates.toArray(new Predicate[0]));
	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, contantsAnyPredicates);
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, contantsAnyPredicates);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.ContantsAll)) {
	    				Expression<String> multiValues = formData.get(criteriaItem.getField()).as(String.class);
	    				
	    				String contantValues = criteriaItem.getValue();
	    				log.info("contantValues:{}", contantValues);
	    				String[] contantValuesArr = contantValues.split(",");
	    				List<Predicate> contantPredicates = new ArrayList<Predicate>();
	    				
	    				for (String contantValue : contantValuesArr) {
	    					contantPredicates.add(criteriaBuilder.like(multiValues, "%"+contantValue+"%")); 	
	    				}
	    				
	    				Predicate contantsAnyPredicates =  criteriaBuilder.and(contantPredicates.toArray(new Predicate[0]));
	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, contantsAnyPredicates);
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, contantsAnyPredicates);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.NotLike)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.notLike( formData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :criteriaBuilder.and(otherPredicate, criteriaBuilder.notLike( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = (otherPredicate == null)?criteriaBuilder.notLike( formData.get(criteriaItem.getField()),"%"+criteriaItem.getValue()+"%" ) :criteriaBuilder.or(otherPredicate, criteriaBuilder.notLike( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
						}
//	    				predicates.add(  cb.equal( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
    			}
    		}
    		if (otherPredicate == null)
    			return criteriaBuilder.conjunction();
    		else
    			return criteriaBuilder.and(otherPredicate);
        });
	}
	
	public List<Form> queryByCriteria(Long typeId, List<ConfigCriteriaItem> criteriaItems){
//		List formFields = Arrays.asList("formNumber", "description");
//		List<Form> forms = new ArrayList<>();
//		List<ConfigCriteriaItem> criteriaItems = criteriaNode.getCriteriaItems();
        log.info("criteriaItems:{}", criteriaItems);
        return formRepository.findAll( (form, query, criteriaBuilder) -> {
        	Join<Form, FormData> formData = form.join("formData");
        	Join<Form, ConfigFormType> configFormType = form.join("configFormType");	//表單TypeId
//        	Predicate formIdPredicate = cb.equal(specForm.get("fId"), formId); //表單id是初始條件
//        	List<Predicate> predicates = new ArrayList<>();
        	
//        	if ((criteriaItems == null) || criteriaItems.size()==0) {
//        		return formIdPredicate;
//        	}
        	
        	Predicate otherPredicate = criteriaBuilder.equal(configFormType.get("cfId"), typeId);	//表單TypeId
        	
        	
    		for (ConfigCriteriaItem criteriaItem: criteriaItems) {
    			log.info("criteriaItem.getValue:{}",criteriaItem.getValue());
    			
    			if (formFields.contains(criteriaItem.getField())){ //判斷是form還是formdata
	    			if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = criteriaBuilder.and(otherPredicate, criteriaBuilder.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
		    				case OR:
		    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
	    				}
//	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = criteriaBuilder.and(otherPredicate, criteriaBuilder.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.like( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
	    				}	
//	    				predicates.add( cb.equal( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.NotLike)) {
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, criteriaBuilder.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.notLike( form.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
        					break;
	    				}
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.NotEqualTo)) {
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, criteriaBuilder.notEqual( form.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.notEqual( form.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				}	  
//    				predicates.add( cb.equal( form.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
    			}
    			else {
    				if (criteriaItem.getOperator().equals(OperatorEnum.EqualTo)) {
    					switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					log.info("criteriaItem.getValue:{}", criteriaItem.getValue());
		    					otherPredicate = criteriaBuilder.and(otherPredicate, criteriaBuilder.equal( formData.get(criteriaItem.getField()), Arrays.asList(criteriaItem.getValue())));
	        					break;
		    				case OR:
		    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	        					break;
    					}	
//    					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}else if(criteriaItem.getOperator().equals(OperatorEnum.IsNotNull)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = criteriaBuilder.and(otherPredicate , criteriaBuilder.isNotNull( formData.get(criteriaItem.getField())));
	        					break;
		    				case OR:
		    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.isNotNull( formData.get(criteriaItem.getField())));
	        					break;
						}
//	    				predicates.add( cb.isNotNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.IsNull)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = criteriaBuilder.and(otherPredicate, criteriaBuilder.isNull( formData.get(criteriaItem.getField())));
	        					break;
		    				case OR:
		    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.isNull( formData.get(criteriaItem.getField())));
	        					break;
						}
//	    				predicates.add( cb.isNull( formData.get(criteriaItem.getField() )));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.Like)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = criteriaBuilder.and(otherPredicate, criteriaBuilder.like( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.like( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
						}
//	    				predicates.add(  cb.equal( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.NotEqualTo)) {
    					switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, criteriaBuilder.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.notEqual( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.In)) {
	    				String inValues = criteriaItem.getValue();
	    				log.info("inValues:{}", inValues);
	    				String[] inValuesArr = inValues.split(",");
	    				In<String> inClause = criteriaBuilder.in( formData.get(criteriaItem.getField()));
	    				for (String inValue : inValuesArr) {
	    				    inClause.value(inValue);
	    				}
	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, inClause);
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, inClause);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.Between)) {    				
	    				String betweenValues = criteriaItem.getValue();
	    				log.info("betweenValues:{}", betweenValues);
	    				String[] betweenValuesArr = betweenValues.split(",");
	    				Predicate betweenPredicate;
	    				if (criteriaItem.getField().startsWith("date")) {
	    					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    					Expression<Date> betweenField = formData.get(criteriaItem.getField());
	   
	    					try {
								betweenPredicate = criteriaBuilder.between(betweenField, format.parse(betweenValuesArr[0]), format.parse(betweenValuesArr[1]));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								betweenPredicate = criteriaBuilder.between(betweenField, new Date(), new Date());
							}
	    				}else {
	    					Expression<Double> betweenField = formData.get(criteriaItem.getField());
	    					betweenPredicate = criteriaBuilder.between(betweenField, Double.valueOf(betweenValuesArr[0]), Double.valueOf(betweenValuesArr[1]));
	    				}

	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, betweenPredicate);
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, betweenPredicate);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.ContantsAny)) {
	    				Expression<String> multiValues = formData.get(criteriaItem.getField()).as(String.class);
	    				
	    				String contantValues = criteriaItem.getValue();
	    				log.info("contantValues:{}", contantValues);
	    				String[] contantValuesArr = contantValues.split(",");
	    				List<Predicate> contantPredicates = new ArrayList<Predicate>();
	    				
	    				for (String contantValue : contantValuesArr) {
	    					contantPredicates.add(criteriaBuilder.like(multiValues, "%"+contantValue+"%")); 	
	    				}
	    				
	    				Predicate contantsAnyPredicates =  criteriaBuilder.or(contantPredicates.toArray(new Predicate[0]));
	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, contantsAnyPredicates);
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, contantsAnyPredicates);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if (criteriaItem.getOperator().equals(OperatorEnum.ContantsAll)) {
	    				Expression<String> multiValues = formData.get(criteriaItem.getField()).as(String.class);
	    				
	    				String contantValues = criteriaItem.getValue();
	    				log.info("contantValues:{}", contantValues);
	    				String[] contantValuesArr = contantValues.split(",");
	    				List<Predicate> contantPredicates = new ArrayList<Predicate>();
	    				
	    				for (String contantValue : contantValuesArr) {
	    					contantPredicates.add(criteriaBuilder.like(multiValues, "%"+contantValue+"%")); 	
	    				}
	    				
	    				Predicate contantsAnyPredicates =  criteriaBuilder.and(contantPredicates.toArray(new Predicate[0]));
	    				
	    				switch(criteriaItem.getLogical()) {
	    				case AND: 
	    					otherPredicate = criteriaBuilder.and(otherPredicate, contantsAnyPredicates);
        					break;
	    				case OR:
	    					otherPredicate = criteriaBuilder.or(otherPredicate, contantsAnyPredicates);
        					break;
    					}	
//					predicates.add( cb.equal( formData.get(criteriaItem.getField()), criteriaItem.getValue()));
	    			}
	    			else if(criteriaItem.getOperator().equals(OperatorEnum.NotLike)) {
	    				switch(criteriaItem.getLogical()) {
		    				case AND: 
		    					otherPredicate = criteriaBuilder.and(otherPredicate, criteriaBuilder.notLike( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
		    				case OR:
		    					otherPredicate = criteriaBuilder.or(otherPredicate, criteriaBuilder.notLike( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	        					break;
						}
//	    				predicates.add(  cb.equal( formData.get(criteriaItem.getField()), "%"+criteriaItem.getValue()+"%"));
	    			}
    			}
    		}
    		if (otherPredicate == null) {
    			log.info("otherPredicate null");
    			return criteriaBuilder.conjunction();
    		}
    		else
    		{
    			log.info("otherPredicate not null");
    			return criteriaBuilder.and(otherPredicate);
    		}
        });
	}
}
