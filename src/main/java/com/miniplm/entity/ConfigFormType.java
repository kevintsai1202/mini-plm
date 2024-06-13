package com.miniplm.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.catalina.webresources.war.Handler;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Proxy(lazy = true)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "MP_CONFIG_FORM_TYPE")
@SQLDelete(sql = "UPDATE MP_CONFIG_FORM_TYPE SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigFormType extends BaseEntity{
	

//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long cfId;
	
	
	
	@Column(name = "NAME",unique = true, nullable = false, length = 45)
	private String name;
	
    @Column(name = "DESCRIPTION", length = 1000)
	private String description;
    
    @JsonIgnore
    @OneToMany(mappedBy = "configFormType", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @OrderBy("VISIBLE DESC, ORDER_BY")
    @Fetch(FetchMode.JOIN)
    private List<ConfigFormField> configFormFields;
    
    @JsonIgnore
    @OneToMany(mappedBy = "configFormType", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Form> forms;
    
//    @JsonIgnore
	@OneToMany(mappedBy = "configFormType", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("ID")
    private List<ConfigFormNumber> configFormNumbers;
	
	@JsonIgnore
	@OneToMany(mappedBy = "configFormType", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("ID")
    private List<ConfigCriteriaNode> configCriteriaNodes;
    
//    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "CONFIG_WORKFLOW_ID", referencedColumnName = "ID")
    private ConfigWorkflow configWorkflow;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(cfId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigFormType)) {
            return false;
        }
        ConfigFormType that = (ConfigFormType) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(cfId, that.cfId);
        return eb.isEquals();
    }
}
