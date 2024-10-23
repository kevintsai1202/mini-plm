package com.miniplm.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miniplm.repository.ConfigFormTypeRepository;
import com.miniplm.request.ConfigWorkflowRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Proxy(lazy = false)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "MP_CONFIG_WORKFLOW")
@SQLDelete(sql = "UPDATE MP_CONFIG_WORKFLOW SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigWorkflow extends BaseEntity{

//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long cwId;
	
	@Column(name = "NAME",unique = true, nullable = false, length = 45)
	private String name;
	
    @Column(name = "DESCRIPTION", length = 1000)
	private String description;
    
    @Column(name = "STATUS", nullable = false)
    private Boolean status= false;
    
    @OneToMany(mappedBy = "cWorkflow", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
	@OrderBy("ORDER_BY")
	@Fetch(FetchMode.SUBSELECT)
    private List<ConfigStep> cSteps;
    
    @JsonIgnore
    @OneToOne(mappedBy = "configWorkflow")
//    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
//    @JoinColumn(name = "CONFIG_FORMTYPE_ID", referencedColumnName = "ID")
    private ConfigFormType cFormType;
    
    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(cwId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigWorkflow)) {
            return false;
        }
        ConfigWorkflow that = (ConfigWorkflow) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(cwId, that.cwId);
        return eb.isEquals();
    }

}
