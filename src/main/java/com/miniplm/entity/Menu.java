package com.miniplm.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miniplm.convert.ConverterListJson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "MP_MENU")
@SQLDelete(sql = "UPDATE MP_MENU SET enabled= 0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class Menu extends BaseEntity implements Comparable<Menu> {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long menuId;
	
	@Column(name = "KEY",unique = true, length = 45)
	private String key;
	
	@Column(name = "LABEL",unique = true, length = 45)
	private String label;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE")
	private MenuEnum menuType;
	
	@Column(name = "LINK", length = 100)
	private String link;
	
	@Column(name = "ICON")
	private String icon;
	
	@OneToMany( mappedBy = "parent" , cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@OrderBy("ORDER_BY")
	@Fetch(FetchMode.SUBSELECT)
	private Set<Menu> children = new TreeSet<>();
	
	@JsonIgnore
	@ToString.Exclude
	@Convert(converter = ConverterListJson.class)
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private Menu parent;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
		@JoinTable(
	          name = "MP_ROLE_FOLDER",
	          joinColumns = @JoinColumn(name = "MENU_ID", referencedColumnName = "ID"),
	          inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
	  )
	private Set<Role> roles = new TreeSet<>();
	
    @Column(name = "ORDER_BY")
    private int orderBy;

	@Override
	public int compareTo(Menu m) {
		if(orderBy > m.orderBy){  
	           return 1;  
	       }else if(orderBy<m.orderBy){  
	           return -1;  
	       }else{  
	          return 0;  
	       }  
	}
}
