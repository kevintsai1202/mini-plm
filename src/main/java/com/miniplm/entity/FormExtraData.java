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
import lombok.ToString;

@Proxy(lazy = true)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "MP_FORM_EXTRA_DATA")
@SQLDelete(sql = "UPDATE MP_FORM_EXTRA_DATA SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class FormExtraData extends BaseEntity{
	

//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long fedId;
	
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(fedId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FormExtraData)) {
            return false;
        }
        FormExtraData that = (FormExtraData) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(fedId, that.fedId);
        return eb.isEquals();
    }
    
    @Column(name = "FIELD_NAME", length = 45)
    private String fieldName;

    @Column(name = "FIELD_DATA", length = 1000)
    private String fieldData;
    
    @Column(name = "ORDER_BY")
    private int orderBy;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FORM_ID", referencedColumnName = "ID")
    private Form form;
}
