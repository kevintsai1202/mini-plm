package com.miniplm.entity;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Proxy(lazy = false)
@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString(exclude = {"tokens", "historys"})
@Table(name = "Z_ACCOUNT")
public class ZAccount implements UserDetails {
@Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "N20_SEQ_ACCOUNT")
  @GenericGenerator(
          name = "N20_SEQ_ACCOUNT",
          strategy = PDMSequenceGenerator.STRATEGY,
          parameters = {
                  @Parameter(name = PDMSequenceGenerator.PARAM_PREFIX, value = "NAC"),
                  @Parameter(
                          name = PDMSequenceGenerator.PARAM_DATETIME_FORMAT,
                          value = PDMSequenceGenerator.DATETIME_FORMAT_YYYYMMDD),
                  @Parameter(name = PDMSequenceGenerator.PARAM_SEQ_LENGTH, value = "4"),
                  @Parameter(name = PDMSequenceGenerator.PARAM_PADDING_CHAR, value = "0")
          })
  @Column(name = "ACCOUNT_ID", length = 30 , nullable = false)
  private String id;

  @Column(name = "ACCOUNT_NAME", length = 30)
  private String username;

  @JsonIgnore
  @Column(name = "PASSWORD", length = 32)
  private String password;

  @Column(name = "ALIAS_NAME", length = 30)
  private String aliasName;
  
  @Column(name = "EMAIL", length = 256)
  private String email;
  
  @OneToOne
  @JoinColumn(name = "TA_ID", referencedColumnName = "ACCOUNT_ID", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  private ZAccount ta;

  @Column(name = "DEPT_ID", length = 30)
  private String deptId;

  @Column(name = "BU", length = 30)
  private String BU;

  @Column(name = "ACCOUNT_TYPE", length = 10)
  private String accountType = "USER";

  @Column(name = "CREATOR", length = 50)
  private String creator;

  @Column(name = "CREATE_DATE", columnDefinition = "TIMESTAMP(6)")
  private Date createDate;
  
  @Column(name = "DELETE_FLAG", length = 1)
  private String deleteFlag;
  
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "MP_USER_ROLE",
          joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ACCOUNT_ID"),
          inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
  )
  @Fetch(FetchMode.SUBSELECT)
  private Set<Role> roles = new TreeSet<>();
  
  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private Set<Token> tokens;
  
//  @OneToMany(mappedBy = "operator" , cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//  @Fetch(FetchMode.SUBSELECT)
//  @JsonIgnore
//  private List<FormHistory> historys;

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
	List<SimpleGrantedAuthority> authorities = new LinkedList<>();
	
	for (Role role : roles) {
		authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
	}
//	authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	return authorities;
}

@Override
public boolean isAccountNonExpired() {
	return true;
}

@Override
public boolean isAccountNonLocked() {
	return true;
}

@Override
public boolean isCredentialsNonExpired() {
	return true;
}

@Override
public boolean isEnabled() {
	return getDeleteFlag().equals("N");
}

}
