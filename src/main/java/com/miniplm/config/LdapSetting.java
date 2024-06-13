package com.miniplm.config;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LdapSetting{
	String username;
	String password;
	String url;
	private List<String> bases;
}