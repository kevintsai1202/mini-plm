package com.miniplm.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miniplm.listener.FormDataListener;
import com.miniplm.listener.FormListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Proxy(lazy = true)
@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString(exclude = {"formData", "actions", "oldData", "creator"})
@Table(name = "MP_FORM")
@SQLDelete(sql = "UPDATE MP_FORM SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
@EntityListeners({AuditingEntityListener.class, FormListener.class})
public class Form extends BaseEntity{
	
	public Form(Form oldData) {
		BeanUtils.copyProperties(oldData, this);
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long fId;	
	
    @Transient
    @JsonIgnore
    private Form oldData;
	
	@Column(name = "FORM_NUMBER",unique = true, length = 45)
	private String formNumber;
	
    @Column(name = "DESCRIPTION", length = 1000)
	private String description;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "FORM_TYPE_ID", referencedColumnName = "ID")
    private ConfigFormType configFormType;
        
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "form")
//    @Fetch(FetchMode.SUBSELECT)
//    private List<FormExtraData> formDatas;
    
    @OneToOne(fetch = FetchType.LAZY)
    private FormData formData;
    
    @OneToOne(fetch = FetchType.EAGER)
    private ConfigWorkflow cWorkflow;
    
    @JsonIgnore
    @OneToMany(mappedBy = "form" , cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Action> actions;    
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "CURR_STEP_ID", referencedColumnName = "ID")
    private ConfigStep currStep;
    
    @CreatedBy
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "CREATOR_ID", referencedColumnName = "ACCOUNT_ID")
    private ZAccount creator;
    
    
    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(fId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Form)) {
            return false;
        }
        Form that = (Form) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(fId, that.fId);
        return eb.isEquals();
    }
//    @OneToMany(mappedBy = "form" , cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @Fetch(FetchMode.SUBSELECT)
//    @JsonIgnore
//    private List<FormHistory> historys;
    
//    @PostConstruct
//    public void init(){
//    	if (configFormType != null)
//    		formNumberUtils.formNumberGenerator(configFormType.getCfId());
//    }
}
