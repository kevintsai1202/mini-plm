package com.miniplm.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "MP_PERMISSION")
@SQLDelete(sql = "UPDATE MP_PERMISSION SET enabled= 0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class Permission extends BaseEntity{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long pId;
	
	@Column(name = "PERMISSION_NAME",unique = true, length = 45)
	private String permissionName;
	
	@Column(name = "HTTP_METHOD")
	private String method = HttpMethod.GET.toString();
	
	@Column(name = "URI_PATTERN",nullable = false, length = 200)
	private String uriPattern;
//	
//	@JsonIgnore
//	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//	@Fetch(FetchMode.SUBSELECT)
//	private Set<Privilege> privileges = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
		@JoinTable(
	          name = "MP_ROLE_PERMISSION",
	          joinColumns = @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID"),
	          inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
	  )
	private Set<Role> roles=new HashSet<>();
}
