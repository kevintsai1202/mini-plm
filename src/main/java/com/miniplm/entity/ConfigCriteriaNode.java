package com.miniplm.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Proxy(lazy = false)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString(exclude = {"configFormType"})
@Table(name = "MP_CONFIG_CRITERIA_NODE")
@SQLDelete(sql = "UPDATE MP_CONFIG_CRITERIA_NODE SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigCriteriaNode extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long cnId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CONFIG_FORM_TYPE_ID", referencedColumnName = "ID")
	private ConfigFormType configFormType;
	
	@Column(name = "CRITERIA_NAME",unique = true, nullable = false, length = 100)
	private String criteriaName;
	
	@OneToMany( mappedBy = "criteriaNode" , cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@OrderBy("ORDER_BY")
	@Fetch(FetchMode.SUBSELECT)
	private List<ConfigCriteriaItem> criteriaItems;
	
	@Override
	public int hashCode() {
		return Objects.hash(cnId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigCriteriaNode other = (ConfigCriteriaNode) obj;
		return Objects.equals(cnId, other.cnId);
	}
	
}
