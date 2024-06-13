package com.miniplm.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ContextSourceConfig {

//	@Bean
//	public LdapConfig ldapConfig() {
//		return new LdapConfig();
//	}

	@Autowired
	private LdapConfig ldapConfig;
	
	@Bean
	public Map<String, LdapContextSource> ldapContextSources() {
		Map<String, LdapContextSource> contextSources = new HashMap<>();
		for (LdapSetting server : ldapConfig.getLdaps()) {
//			System.out.println("ldap server:"+ server);
			log.info("ldap server {}", server);
			LdapContextSource contextsource = new LdapContextSource();
			contextsource.setUrl(server.getUrl());
			contextsource.setUserDn(server.getUsername());
			contextsource.setPassword(server.getPassword());
//			contextsource.setBase(server);
			contextSources.put(server.getUrl(), contextsource);
		}
		log.info("contextSources: {}",contextSources);
//		System.out.println("contextSources:"+contextSources);
//		System.out.println("Configuration contextSources size:"+contextSources.size());
		return contextSources;
	}
}
