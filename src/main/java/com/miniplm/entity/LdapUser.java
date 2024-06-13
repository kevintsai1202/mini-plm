package com.miniplm.entity;

import javax.naming.Name;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.Data;

@Data
@Entry(objectClasses = "person")
//@Entry(base = "OU=CEC, dc=chicony, dc=local", objectClasses = {"OrganizationalPerson", "Person", "top"})
public class LdapUser {
	@Id
	private Name id;
		
	@DnAttribute(value = "uid", index = 0) 
	private String uid;
	
    @Attribute(name = "sAMAccountName")
    private String loginName;
    
    @Attribute(name = "cn") 
	private String cn;
    
	@Attribute(name="mail")
	private String mail;
}
