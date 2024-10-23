package com.miniplm.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miniplm.convert.ConverterListJson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = {"roles"})
@Table(name = "MP_PRIVILEGE")
@SQLDelete(sql = "UPDATE MP_PRIVILEGE SET enabled= 0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class Privilege extends BaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long privilegeId;
	
	@Column(name = "OBJ_ID", nullable = false)
	private Long objId;
	
	@Column(name = "PRIVILEGE_NAME", nullable = true, length = 45)
	private String privilegeName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PRIVILEGE", nullable = true, length = 45)
	private PrivilegeEnum privilege;
	
    @Convert(converter = ConverterListJson.class)
    @Column(name = "FIELDS", length = 2000)
	private List<String> fields;	//edit field
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "TABLES", length = 2000)
	private List<Integer> tables;	//edit field
    
    
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
		@JoinTable(
	          name = "MP_ROLE_PRIVILEGE",
	          joinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "ID"),
	          inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
	  )
	private Set<Role> roles=new HashSet<>();

}
