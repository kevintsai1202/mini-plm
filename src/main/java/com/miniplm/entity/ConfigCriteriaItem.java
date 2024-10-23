package com.miniplm.entity;

import java.util.LinkedList;
import java.util.List;

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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miniplm.convert.ConverterListJson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Proxy(lazy = false)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "MP_CONFIG_CRITERIA_ITEM")
@SQLDelete(sql = "UPDATE MP_CONFIG_CRITERIA_ITEM SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigCriteriaItem extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long ciId;
	
	@Column(name = "FIELD", nullable = false, length = 45)
	private String field;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "OPERATOR", nullable = false, length = 45)
	private OperatorEnum operator;
	
//	@Convert(converter = ConverterListJson.class)
	@Column(name = "VALUE", nullable = true, length = 2000)
	private String value;
	
//    @Convert(converter = ConverterListJson.class)
//    @Column(name = "MULTI_VALUE", length = 2000)
//	private LinkedList<Object> multiValue;
	
    @Enumerated(EnumType.STRING)
	@Column(name = "LOGICAL", nullable = false, length = 45)
	private LogicalEnum Logical;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private ConfigCriteriaNode criteriaNode;
	
//    @Column(name = "ORDER_BY")
//    private int orderBy;
    
    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(ciId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigCriteriaItem)) {
            return false;
        }
        ConfigCriteriaItem that = (ConfigCriteriaItem) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(ciId, that.ciId);
        return eb.isEquals();
    }
}
