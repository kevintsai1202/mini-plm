package com.miniplm.listener;

import javax.persistence.PostLoad;

import org.springframework.stereotype.Component;

import com.miniplm.entity.Form;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FormListener {
//
//    
//    @Autowired
//	private FormHistoryRepository formHistoryRepository;
	
	@PostLoad
    public void postLoad(Form form) {
//        log.info("Save old Form data");
		form.setOldData(new Form(form));
    }
	
//	@PostUpdate
//    public void postUpdate(Form form) {
//		Map<String, Object[]> differences = new HashMap<>();
////		System.out.println("已更新 FormData: " + formData);
////		log.info("old Form: {}", form.getOldData());
////        log.info("new Form: {}", form);
//        
//        BeanWrapper oldBean = new BeanWrapperImpl(form.getOldData());
//        BeanWrapper newBean = new BeanWrapperImpl(form);
//        PropertyDescriptor properties[] = BeanUtils.getPropertyDescriptors(Form.class);
//        
//        for (PropertyDescriptor property : properties) {
//        	String propertyName = property.getName();
//            
//        	Object value1 = oldBean.getPropertyValue(propertyName);
//            Object value2 = newBean.getPropertyValue(propertyName);
//
//            if ((value1 != null && !value1.equals(value2)) || (value1 == null && value2 != null)) {
//                List<String> excepts = new ArrayList<>();
//                excepts.add("oldData");
//                excepts.add("formData");
//                excepts.add("actions");
//                excepts.add("updateTime");
//                excepts.add("currStep");
//                if (!excepts.contains(propertyName)) {
//                	differences.put(propertyName, new Object[]{value1, value2});
//                }
//            }
//        }
//        
//        if (differences.size()>0) {
//        	StringBuffer sb = new StringBuffer();
//        	differences.forEach((field, values) -> {
//        		sb.append(field + ": " + values[0] + " -> " + values[1] + " ");
//            });
//        	log.info("Modify details:{}", sb.toString());
//        	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        	ZAccount user = null;
//    		if (authentication != null && authentication.isAuthenticated()) {
//    			user = (ZAccount) authentication.getPrincipal();
//    		}
//    		
//        	
//        	FormHistory history = FormHistory.builder()
//        			.detail(sb.toString())
//        			.form(form)
//        			.operator(user)
//        			.step(form.getCurrStep())
//        			.type(ChangeTypeEnum.Modify)
//        			.build();
//        	
//        	applicationEventPublisher.publishEvent(new FormUpdateEvent(history));
////        	formHistoryRepository.save(history);
//        }
//    }
}
