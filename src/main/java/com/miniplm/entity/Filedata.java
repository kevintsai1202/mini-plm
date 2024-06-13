package com.miniplm.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.miniplm.convert.ConverterListJson;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MP_FILEDATA")
@SQLDelete(sql = "UPDATE MP_FILEDATA SET ENABLED = 0 WHERE ID=?")
@Where(clause = "enabled = 1")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
//@EntityListeners(AuditingEntityListener.class)
public class Filedata extends BaseEntity{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long fdId;
	
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(fdId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Filedata)) {
            return false;
        }
        Filedata that = (Filedata) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(fdId, that.fdId);
        return eb.isEquals();
    }
	
	@Column(name = "FILE_NAME", nullable = false, length = 256)
	private String fileName;
	
	@Column(name = "STORAGE_UUID", nullable = false, length = 45)
	private String storageUuid;
	
	@Column(name = "STORAGE_FOLDER", nullable = false, length = 10) //upload date
	private String storageFolder;
	
	@Column(name = "FILE_SIZE", nullable = false) //byte
	private Long fileSize;
	
//	@CreatedBy
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "ACCOUNT_ID")
	private ZAccount owner;
	
    @Convert(converter = ConverterListJson.class)
    @Column(name = "SHAREDUSERS", length = 2000)
	private LinkedList<Object> sharedUsers;
	
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "FORM_ID", referencedColumnName = "ID")
	private Form form;
    
    @Column(name = "DATA_INDEX", length = 45) //表單自訂UPLOAD FIELD
    private String dataIndex;
}
