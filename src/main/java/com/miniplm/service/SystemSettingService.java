package com.miniplm.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniplm.entity.SystemSetting;
import com.miniplm.repository.SystemSettingRepository;

@Service
public class SystemSettingService {

	@Autowired
	SystemSettingRepository systemSettingRepository;
	
	@Transactional
	public void init() {
		if (systemSettingRepository.count()==0) {
		systemSettingRepository.save(
				SystemSetting.builder()
				.sId(1000000001L)
				.name("Approve subject template")
				.value("表單編號:$$formnumber$$需要您簽核")
				.build()
				);
		systemSettingRepository.save(
				SystemSetting.builder()
				.sId(1000000002L)
				.name("Approve body template")
				.value("<h3>表單編號:$$formnumber$$</h3>"
						+ "<p>表單描述:$$formdesc$$</p>"
						+ "<p>表單連結:$$formlink$$</p>"
						+ "<p>有表單需要您簽核,請由上方連結進入系統簽核</p>")
				.build()
				);
		systemSettingRepository.save(
				SystemSetting.builder()
				.sId(1000000003L)
				.name("Notice subject")
				.value("表單編號:$$formnumber$$通知")
				.build()
				);
		systemSettingRepository.save(
				SystemSetting.builder()
				.sId(1000000004L)
				.name("Notice body template")
				.value("<h3>表單編號:$$formnumber$$</h3>"
						+ "<p>表單描述:$$formdesc$$</p>"
						+ "<p>表單連結:$$formlink$$</p>"
						+ "<p>表單已進入$$step$$，請點選連結進入系統觀看</p>")
				.build()
				);
		}
		System.out.println("System setting init done!");
		long systemCount = systemSettingRepository.count();
		System.out.println("目前有"+systemCount+"筆系統設定");
	}
}
