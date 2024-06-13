package com.miniplm.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import com.miniplm.config.LdapConfig;
import com.miniplm.config.LdapSetting;
import com.miniplm.entity.LdapUser;
import com.miniplm.request.AuthRequest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@Slf4j
public class LdapService {
//	@Resource
//	LdapUserRepository ldapUserRepository;

//	@Autowired
//	private LdapTemplate ldapTemplate;

	@Resource
	private Map<String, LdapTemplate> ldapTemplates;

	@Autowired
	private LdapConfig ldapConfig;

	public LdapUser authenticate(String loginName, String password) {
		EqualsFilter filter = new EqualsFilter("sAMAccountName", loginName);
		boolean result = false;
//        System.out.println("bases:"+bases);
//        LdapSetting ldap = ldapConfig.getLdaps().get(0);
//		System.out.println("service ldapTemplates size:" + ldapTemplates.size());

		for (LdapSetting ldap : ldapConfig.getLdaps()) {
			String url = ldap.getUrl();
			LdapTemplate ldapTemplate = ldapTemplates.get(url);
			log.info("url: {}" , url);
//			System.out.println("url:" + url);
			log.info("ldap.getBases().size(): {}" , ldap.getBases().size());
//			System.out.println("ldap.getBases().size():" + ldap.getBases().size());
			log.info("ldapTemplate: {}", ldapTemplate);
//			System.out.println("ldapTemplate:" + ldapTemplate);
			for (String base : ldap.getBases()) {
				log.info("base: {}", base);
//				System.out.println("base:" + base);
				log.info("filter: {}" , filter);
//				System.out.println("filter:" + filter);
				log.info("password: {}" , password);
//				System.out.println("password:" + password);
				LdapQuery query = query().base(base).where("sAMAccountName").is(loginName); // .or("cn").is(keyword);
				List<LdapUser> ldapUsers = ldapTemplate.find(query, LdapUser.class);
				if (ldapUsers.size() > 0) {
					result = ldapTemplate.authenticate(base, filter.toString(), password);
					if (result) {
						log.info("ldapUser 驗證成功: {}" ,url);
//						System.out.println(url + " 驗證成功");
						return ldapUsers.get(0);
					} else {
						log.info("密碼錯誤或失效: {}", url);
//						System.out.println(url + " 密碼錯誤或失效");
						return null;
					}
				}
			}
		}
		
		log.info("無此用戶");
//		System.out.println("無此用戶");
		return null;
	}

	public List<LdapUser> searchLdapUser(String keyword) {
//        LdapQuery query = query().where("sAMAccountName").is(keyword).or("cn").is(keyword);

		for (LdapSetting ldap : ldapConfig.getLdaps()) {
//			LdapSetting ldap = ldapConfig.getLdaps().get(0);
			String urls = ldap.getUrl();
			LdapTemplate ldapTemplate = ldapTemplates.get(urls);
			for (String base : ldap.getBases()) {
				LdapQuery query = query().base(base).where("sAMAccountName").is(keyword); // .or("cn").is(keyword);
				List<LdapUser> ldapUsers = ldapTemplate.find(query, LdapUser.class);
				if (ldapUsers.size() > 0)
					return ldapUsers;
			}
		}
		return new ArrayList<>();
	}

	public LdapUser loginByLdap(AuthRequest request) throws UserPrincipalNotFoundException, AuthenticationException {
		LdapUser ldapUser = null;
//		List<LdapUser> users = null;
		ldapUser = authenticate(request.getUsername(), request.getPassword());
		if (ldapUser != null) {
			log.info("Login success");
//			System.out.println("Login success");
//			users = searchLdapUser(request.getUsername());
//			System.out.println("查出 "+users.size()+" 筆 Ldap 資料");
//			ldapUser = users.get(0);
			log.info("ldap user email: {}", ldapUser.getMail());
//			System.out.println("ldap user email:" + ldapUser.getMail());
		} else {
			log.info("Login fail");
//			System.out.println("Login fail");
			throw new AuthenticationException("帳號或密碼錯誤");
		}
		return ldapUser;
	}

	public LdapUser loginByLdap(MultiValueMap<String, String> formData)
			throws UserPrincipalNotFoundException, AuthenticationException {
		LdapUser ldapUser = null;
//		List<LdapUser> users = null;
		ldapUser = authenticate(formData.get("username").toString(), formData.get("password").toString());
		if (ldapUser != null) {
			log.info("Login success");
			System.out.println("Login success");
			log.info("ldap user email: {}" , ldapUser.getMail());
//			System.out.println("ldap user email:" + ldapUser.getMail());
		} else {
			log.info("Login fail");
//			System.out.println("Login fail");
			throw new AuthenticationException("帳號或密碼錯誤");
		}
//		LdapUser ldapUser = null;
//		List<LdapUser> users = searchLdapUser(formData.get("username").toString());
//		System.out.println("查出 "+users.size()+" 筆 Ldap 資料");
//		if (users.size()>0) {
//			ldapUser = users.get(0);
//			System.out.println("ldap user email:"+ldapUser.getMail());
//		}else {
//			throw new AuthenticationCredentialsNotFoundException(formData.get("username").toString()+" not found");
//		}
		return ldapUser;
	}

}
