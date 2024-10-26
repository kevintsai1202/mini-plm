package com.miniplm.entity;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "MP_USERGROUP")
@SQLDelete(sql = "UPDATE MP_USERGROUP SET enabled = 0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name = "MP_SEQUENCE_GENERATOR", sequenceName = "MP_SEQ", initialValue = 1, allocationSize = 1)
public class UserGroup extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MP_SEQUENCE_GENERATOR")
	@Column(name = "ID", unique = true, nullable = false)
	private Long ugId;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "MP_USERGROUP_ROLE", joinColumns = @JoinColumn(name = "USERGROUP_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
	@Fetch(FetchMode.SUBSELECT)
	private Set<Role> roles = new TreeSet<>();

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "MP_USER_USERGROUP", joinColumns = @JoinColumn(name = "USERGROUP_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ACCOUNT_ID"))
	private Set<ZAccount> users = new TreeSet<>();
}
