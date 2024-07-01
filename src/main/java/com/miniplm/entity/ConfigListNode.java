package com.miniplm.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Proxy(lazy = false)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "MP_CONFIG_LISTNODE")
@SQLDelete(sql = "UPDATE MP_CONFIG_LISTNODE SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigListNode extends BaseEntity{
	

//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long clnId;
	
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(clnId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigListNode)) {
            return false;
        }
        ConfigListNode that = (ConfigListNode) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(clnId, that.clnId);
        return eb.isEquals();
    }
	
	@Column(name = "NAME",unique = true, nullable = false, length = 100)
	private String name;
	
	@Column(name = "DESCRIPTION", length = 255)
	private String description;
	
//	@JsonIgnore
	@OneToMany( mappedBy = "listNode" , cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@OrderBy("ORDER_BY")
	@Fetch(FetchMode.SUBSELECT)
	private List<ConfigListItem> listItems;
}
