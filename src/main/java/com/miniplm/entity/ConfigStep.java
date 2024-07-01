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
import javax.persistence.OrderBy;
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
import com.miniplm.convert.ConverterConfigStepJson;
import com.miniplm.convert.ConverterListJson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Proxy(lazy = false)
@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString(exclude = {"nextStep"})
@Table(name = "MP_CONFIG_STEP")
@SQLDelete(sql = "UPDATE MP_CONFIG_STEP SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigStep extends BaseEntity{
	public enum Status {
		wait, process, finish, error 
	}

//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long csId;
	
	@Column(name = "STEP_NAME",unique = true, nullable = false, length = 45)
	private String stepName;
	
    @Column(name = "DESCRIPTION", length = 1000)
	private String description;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "APPROVERS", length = 2000)
    private LinkedList<Object> approvers = new LinkedList<>();
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "NOTIFIERS", length = 2000)
    private LinkedList<Object> notifiers = new LinkedList<>();
    
//    @Column(name = "STATUS", length = 10)
//    private String status; // wait process finish error
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "CONFIG_WORKFLOW_ID", referencedColumnName = "ID")
    private ConfigWorkflow cWorkflow;
    
    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "NEXT_STEP_ID", referencedColumnName = "ID")
    private ConfigStep nextStep;
    
//    @JsonIgnore
    @Convert(converter = ConverterConfigStepJson.class)
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "REJECT_STEP_ID", referencedColumnName = "ID")
    private ConfigStep rejectStep;
    
    @Column(name = "ORDER_BY", nullable = false)
    private int orderBy;
    
    @JsonIgnore
    @OneToMany(mappedBy = "cStep" , cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("ORDER_BY")
	@Fetch(FetchMode.SUBSELECT)
    private List<ConfigStepCriteria> cStepCriterias;
    
    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(csId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigStep)) {
            return false;
        }
        ConfigStep that = (ConfigStep) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(csId, that.csId);
        return eb.isEquals();
    }
}
