package com.miniplm.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Proxy(lazy = false)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "MP_CONFIG_LISTITEM")
@SQLDelete(sql = "UPDATE MP_CONFIG_LISTITEM SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigListItem extends BaseEntity{
	
	
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long cliId;
	
	@Column(name = "KEY", nullable = false, length = 45)
	private String key;
	
	@Column(name = "VALUE", nullable = false, length = 100)
	private String value;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private ConfigListNode listNode;
	
    @Column(name = "ORDER_BY")
    private int orderBy;
    
    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(cliId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigListItem)) {
            return false;
        }
        ConfigListItem that = (ConfigListItem) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(cliId, that.cliId);
        return eb.isEquals();
    }
}
