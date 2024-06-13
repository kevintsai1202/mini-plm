package com.miniplm.entity;


import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.miniplm.convert.ConverterListJson;

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
@Builder
@Entity
@ToString(exclude = {"cStep", "cCriteriaNode"})
@Table(name = "MP_CONFIG_STEP_CRITERIA")
@SQLDelete(sql = "UPDATE MP_CONFIG_STEP_CRITERIA SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigStepCriteria extends BaseEntity{

	//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long cscId;
    
	@JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE , fetch = FetchType.EAGER)
    @JoinColumn(name = "STEP_ID", referencedColumnName = "ID")
    private ConfigStep cStep;
    
    @ManyToOne(cascade = CascadeType.MERGE , fetch = FetchType.EAGER)
    @JoinColumn(name = "CRITERIA_NODE_ID", referencedColumnName = "ID")
    private ConfigCriteriaNode cCriteriaNode;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "APPROVERS", length = 2000)
    private LinkedList<Object> approvers;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "NOTIFIERS", length = 2000)
    private LinkedList<Object> notifiers;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "REQUIRED_FIELDS", length = 2000)
    private LinkedList<Object> requiredFields;
       
    @Column(name = "ORDER_BY", nullable = false)
    private int orderBy;
    
    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(cscId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigStepCriteria)) {
            return false;
        }
        ConfigStepCriteria that = (ConfigStepCriteria) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(cscId, that.cscId);
        return eb.isEquals();
    }
}
