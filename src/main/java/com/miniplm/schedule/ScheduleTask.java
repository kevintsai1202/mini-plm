package com.miniplm.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.miniplm.entity.Action;
import com.miniplm.repository.ActionRepository;
import com.miniplm.repository.SystemSettingRepository;
import com.miniplm.service.MailService;
import com.miniplm.service.TemplateWordEnum;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduleTask {

	@Autowired
	private ActionRepository actionRepository;
	
	@Autowired
	private SystemSettingRepository systemSettingRepository;
	
	@Autowired
	private MailService mailService;
	
	@Value("${server-url}")
	private String serverUrl;
	
	private Map<String, String> templateMap = new HashMap<>();
	
	@Scheduled(fixedRateString = "${fixedRate}")
	public void checkAndSendEmails() throws MessagingException {
		processApproveActions();
		processNoticeActions();
	}
	
	
	private void processApproveActions() throws MessagingException {
		//approve
		List<Action> approveActions = actionRepository.findByTypeAndNoticeFlagAndCanNoticeFlag("A", false, true);
//		System.out.println(approveActions);
		
		for (Action approveAction: approveActions) {
			String formNumber = approveAction.getForm().getFormNumber();
			String formDesc = approveAction.getForm().getDescription();
			String formStep = approveAction.getForm().getCurrStep().getStepName();
			String formLink = serverUrl+"/user/formnumber/"+formNumber+"/read";
			String serverLink = serverUrl;
			String toEmail = approveAction.getUser().getEmail();
			log.info("email: {}",toEmail);
//			System.out.println("email:"+toEmail);
			log.info("User: {}", approveAction.getUser());
//			System.out.println("User:"+approveAction.getUser());
			
			
			templateMap.put(TemplateWordEnum.formnumber.name() , formNumber);
			templateMap.put(TemplateWordEnum.formdesc.name(), formDesc);
			templateMap.put(TemplateWordEnum.formstep.name(), formStep);
			templateMap.put(TemplateWordEnum.formlink.name() , formLink);
			templateMap.put(TemplateWordEnum.serverlink.name() , serverLink);
			
			mailService.sendEmail(toEmail,
					systemSettingRepository.findByName("Approve subject template").getValue(), 
					systemSettingRepository.findByName("Approve body template").getValue(),
					templateMap
					);
			approveAction.setNoticeFlag(true);
			actionRepository.save(approveAction);
		}
	}
	
	private void processNoticeActions() throws MessagingException {
		//notice
		List<Action> noticeActions = actionRepository.findByTypeAndNoticeFlagAndCanNoticeFlag("N", false, true);
//		System.out.println(noticeActions);
		for (Action noticeAction: noticeActions) {
			String formNumber = noticeAction.getForm().getFormNumber();
			String formDesc = noticeAction.getForm().getDescription();
			String formStep = noticeAction.getForm().getCurrStep().getStepName();
			String formLink = "<a href='"+serverUrl+"/user/formnumber/"+formNumber+"/read"+"'>"+serverUrl+"/user/formnumber/"+formNumber+"/read"+"</a>";
			String serverLink = serverUrl;
			String toEmail = noticeAction.getUser().getEmail();
			log.info("email: {}", toEmail);
//			System.out.println("email:"+toEmail);
			log.info("User: {}", noticeAction.getUser());
//			System.out.println("User:"+noticeAction.getUser());
			
			
			templateMap.put(TemplateWordEnum.formnumber.name() , formNumber);
			templateMap.put(TemplateWordEnum.formdesc.name(), formDesc);
			templateMap.put(TemplateWordEnum.formstep.name(), formStep);
			templateMap.put(TemplateWordEnum.formlink.name() , formLink);
			templateMap.put(TemplateWordEnum.serverlink.name() , serverLink);
			
			mailService.sendEmail(toEmail,
					systemSettingRepository.findByName("Notice subject template").getValue(), 
					systemSettingRepository.findByName("Notice body template").getValue(),
					templateMap
					);
			noticeAction.setNoticeFlag(true);
			actionRepository.save(noticeAction);
		}
	}
}
