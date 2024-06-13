package com.miniplm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.miniplm.entity.ConfigCriteriaItem;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigStepCriteria;
import com.miniplm.entity.Form;
import com.miniplm.entity.OperatorEnum;
import com.miniplm.entity.Privilege;
import com.miniplm.entity.PrivilegeEnum;
import com.miniplm.repository.ConfigCriteriaItemRepository;
import com.miniplm.repository.ConfigCriteriaNodeRepository;
import com.miniplm.repository.ConfigFormFieldRepository;
import com.miniplm.repository.ConfigStepCriteriaRepository;
import com.miniplm.repository.ConfigStepRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.service.ActionService;
import com.miniplm.service.AuthorizationService;
import com.miniplm.service.ConfigFormFieldService;
import com.miniplm.service.FormDetailsService;
import com.miniplm.service.QueryService;

@SpringBootTest
class MiniPlmApplicationTests {
	
	@Autowired
	ConfigCriteriaNodeRepository configCriteriaNodeRepository;
	
	@Autowired
	ConfigCriteriaItemRepository configCriteriaItemRepository;
	
	@Autowired
	ConfigFormFieldService configFormFieldService;
	
	@Autowired
	QueryService queryService;
	
	@Autowired
	FormDetailsService formDetailsService;
	
	@Autowired
	ConfigStepRepository configStepRepository;
	
	@Autowired
	ConfigStepCriteriaRepository configStepCriteriaRepository;
	
	@Autowired
	FormRepository formRepository;
	
	@Autowired
	ActionService actionService;
	
	@Autowired
	AuthorizationService authorizationService;
	
//	@Test
//	@Transactional
//	@Rollback(false)
//	public void test2() {
//		ConfigStep step = configStepRepository.getReferenceById(5430L);
//		
//		System.out.println(step);
//	}
//	
//	@Test
//	@Transactional
//	@Rollback(false)
//	public void test3() {
//		try {
//		ConfigCriteriaNode node = configCriteriaNodeRepository.getReferenceById(7301L);
//		ConfigCriteriaItem item = ConfigCriteriaItem.builder()
//				.field("formNumber")
//				.criteriaNode(node)
//				.operator(OperatorEnum.EqualTo)
//				.value("MP2024050002-AB")
//				.build();
//			configCriteriaItemRepository.save(item);
//		} catch (Exception e) {
//            // 捕获异常并输出日志
//            e.printStackTrace();
//        }
//	}
//	
//	@Test
//	@Transactional
//	@Rollback(false)
//	public void test4() {
//		try {
//			ConfigCriteriaNode node = configCriteriaNodeRepository.getReferenceById(7301L);
//			List<ConfigCriteriaItem> items = node.getCriteriaItems();
//			
//			List<Form> forms = formDetailsService.findFormByCriteria(node);
//			
//			System.out.println(forms.size());
//		
//		} catch (Exception e) {
//            // 捕获异常并输出日志
//            e.printStackTrace();
//        }
//	}
//	
//	@Test
//	@Transactional
//	@Rollback(false)
//	public void test5() {
//		try {
//			ConfigCriteriaNode node = configCriteriaNodeRepository.getReferenceById(7301L);
//			ConfigStep step = configStepRepository.getReferenceById(5430L);
//			ConfigStepCriteria csc = ConfigStepCriteria.builder()
//					.cCriteriaNode(node)
//					.cStep(step)
//					.build();
//			
//			configStepCriteriaRepository.save(csc);
//			
//		} catch (Exception e) {
//            // 捕获异常并输出日志
//            e.printStackTrace();
//        }
//	}
//	
//	@Test
//	@Transactional
//	@Rollback(false)
//	public void test6() {
//		try {
//			ConfigStep step = configStepRepository.getReferenceById(5430L);
//			List<Object> approvers = queryService.getApproversByAllStepCriterias(5683L, step);
//			System.out.println(approvers);
//		} catch (Exception e) {
//            // 捕获异常并输出日志
//            e.printStackTrace();
//        }
//	}
//	
//	@Test
//	@Transactional
//	@Rollback(false)
//	public void test7() {
//		try {
//			ConfigStep step = configStepRepository.getReferenceById(5430L);
//			List<ConfigStepCriteria> scs =configStepCriteriaRepository.findBycStep(step);
//			System.out.println(scs.size());
////			System.out.println(approvers);
//		} catch (Exception e) {
//            // 捕获异常并输出日志
//            e.printStackTrace();
//        }
//	}	
	
	@Test
	@Transactional
	@Rollback(false)
	public void test8() {
		Set<String> fields = authorizationService.getUserModifyFields("kevintsai", 6424L);
		System.out.println(fields);
	}
}
