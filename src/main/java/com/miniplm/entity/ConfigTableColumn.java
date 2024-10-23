package com.miniplm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "MP_CONFIG_TABLE_COLUMN")
@SQLDelete(sql = "UPDATE MP_CONFIG_TABLE_COLUMN SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigTableColumn extends BaseEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long ctcId;
	
    @JsonIgnore
    @ManyToOne
	@JoinColumn(name = "CONFIG_TABLE_ID", referencedColumnName = "ID")
    private ConfigTableHeader configTableHeader;
	
    @Enumerated(EnumType.STRING)
	@Column(name = "COL_TYPE", nullable = false, length = 45)
	private DataTypeEnum colType;
	
	@Column(name = "MULTIPLE", nullable = false)
	private Boolean multiple = false;
	
	@Column(name = "DATA_INDEX", nullable = false, length = 45)
	private String dataIndex;
	
    @Column(name = "COL_NAME", nullable = false, length = 45)
	private String colName;
    
    @Column(name = "REQUIRED", nullable = false)
    private Boolean required = false;
    
    @Column(name = "VISIBLE", nullable = false)
    private Boolean visible = false;
    
    @Column(name = "HTML_FORMAT")
    private Boolean htmlFormat = false;
    
    @Column(name = "STEP", nullable = false)
    private int step = 0;
    
    @Column(name = "PATTERN", nullable = true, length = 100)
    private String pattern;
    
    @Column(name = "PATTERN_MSG", nullable = true, length = 100)
    private String patternMsg;
    
    @Column(name = "ORDER_BY", nullable = false)
    private int orderBy;
    
    @ManyToOne
    @JoinColumn(name = "LISTNODE_ID", referencedColumnName = "ID")
    private ConfigListNode configListNode;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(ctcId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigTableColumn)) {
            return false;
        }
        ConfigTableColumn that = (ConfigTableColumn) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(ctcId, that.ctcId);
        return eb.isEquals();
    }
    
    public ConfigTableColumn(DataTypeEnum colType, String dataIndex, int orderBy, ConfigTableHeader configTableHeader) {
    	this.colType = colType;
    	this.dataIndex = dataIndex;
    	this.colName = dataIndex;
    	this.orderBy = orderBy;
    	this.required = false;
    	this.multiple = false;
    	this.visible = false;
    	this.htmlFormat = false;
    	this.configTableHeader = configTableHeader;
    }
    
    public ConfigTableColumn(DataTypeEnum colType, String dataIndex, int orderBy,ConfigTableHeader configTableHeader, Boolean multiple) {
    	this.colType = colType;
    	this.dataIndex = dataIndex;
    	this.colName = dataIndex;
    	this.orderBy = orderBy;
    	this.required = false;	
    	this.multiple = multiple;
    	this.visible = false;
    	this.htmlFormat = false;
    	this.configTableHeader = configTableHeader;
    }
}
