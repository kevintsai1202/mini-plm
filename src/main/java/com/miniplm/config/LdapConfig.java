package com.miniplm.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "ldap-config")
@Data
public class LdapConfig {
	private List<LdapSetting> ldaps;
}