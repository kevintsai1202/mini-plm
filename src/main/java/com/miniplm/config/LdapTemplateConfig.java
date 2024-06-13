package com.miniplm.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LdapTemplateConfig {

//	@Bean
//	public LdapConfig ldapConfig() {
//		return new LdapConfig();
//	}

	@Resource
	private Map<String, LdapContextSource> ldapContextSources;
	
	@Bean
	public Map<String, LdapTemplate> ldapTemplates() {
		log.info("ldapContextSources: {}", ldapContextSources);
//		System.out.println("ldapContextSources:"+ldapContextSources);
		Map<String, LdapTemplate> ldapTemplates = new HashMap<>();
		for (String urls: ldapContextSources.keySet()) {
			log.info("create ldapTemplate: {}", urls);
//			System.out.println("create ldapTemplate:"+urls);
			LdapContextSource lcs = ldapContextSources.get(urls);
			lcs.afterPropertiesSet();
			log.info("LdapContextSource: {}", lcs);
//			System.out.println("LdapContextSource:"+lcs);
			LdapTemplate ldapTemplate = new LdapTemplate(lcs);
			ldapTemplates.put(urls, ldapTemplate);
		}
		log.info("Configuration ldapTemplates size: {}", ldapTemplates.size());
//		System.out.println("Configuration ldapTemplates size:"+ldapTemplates.size());
		return ldapTemplates;
	}
}
