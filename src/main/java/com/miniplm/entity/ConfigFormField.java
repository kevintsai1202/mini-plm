package com.miniplm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Proxy(lazy = true)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "MP_CONFIG_FORM_FIELD")
@SQLDelete(sql = "UPDATE MP_CONFIG_FORM_FIELD SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigFormField extends BaseEntity{
	
	
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long cffId;
	
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(cffId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigFormField)) {
            return false;
        }
        ConfigFormField that = (ConfigFormField) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(cffId, that.cffId);
        return eb.isEquals();
    }
	
	@Column(name = "FIELD_TYPE", nullable = false, length = 45)
	private String fieldType;
	
	@Column(name = "MULTIPLE", nullable = false)
	private Boolean multiple = false;
	
	@Column(name = "DATA_INDEX", nullable = false, length = 45)
	private String dataIndex;
	
    @Column(name = "FIELD_NAME", nullable = false, length = 45)
	private String fieldName;
    
    @Column(name = "REQUIRED", nullable = false)
    private Boolean required = false;
    
    @Column(name = "VISIBLE", nullable = false)
    private Boolean visible = false;
    
    @Column(name = "HTML_FORMAT")
    private Boolean htmlFormat = false;
    
    @Column(name = "STEP", nullable = false)
    private int step = 0;
    
    @Column(name = "GROUPS", nullable = true, length = 45)
    private String groups;
    
    @Column(name = "PATTERN", nullable = true, length = 100)
    private String pattern;
    
    @Column(name = "PATTERN_MSG", nullable = true, length = 100)
    private String patternMsg;
    
    @Column(name = "ORDER_BY", nullable = false)
    private int orderBy;
    
    @JsonIgnore
    @ManyToOne
	@JoinColumn(name = "FORM_TYPE_ID", referencedColumnName = "ID")
    private ConfigFormType configFormType;
    
    @OneToOne
    @JoinColumn(name = "LISTNODE_ID", referencedColumnName = "ID")
    private ConfigListNode configListNode;

    
    public ConfigFormField(String fieldType, String dataIndex, int orderBy, ConfigFormType formType) {
    	this.fieldType = fieldType;
    	this.dataIndex = dataIndex;
    	this.fieldName = dataIndex;
    	this.orderBy = orderBy;
    	this.required = false;
    	this.multiple = false;
    	this.visible = false;
    	this.htmlFormat = false;
    	this.configFormType = formType;
    }
    
    public ConfigFormField(String fieldType, String dataIndex, int orderBy, ConfigFormType formType, Boolean multiple) {
    	this.fieldType = fieldType;
    	this.dataIndex = dataIndex;
    	this.fieldName = dataIndex;
    	this.orderBy = orderBy;
    	this.required = false;	
    	this.multiple = multiple;
    	this.visible = false;
    	this.htmlFormat = false;
    	this.configFormType = formType;
    }
}
