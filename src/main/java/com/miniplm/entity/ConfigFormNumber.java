package com.miniplm.entity;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.mapstruct.ap.shaded.freemarker.template.utility.StringUtil;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Proxy(lazy = false)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "MP_CONFIG_FORM_NUMBER")
@SQLDelete(sql = "UPDATE MP_CONFIG_FORM_NUMBER SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class ConfigFormNumber extends BaseEntity{
	//FORM_NUMBER = PREFIX+MIDFIX+PADDING-SEQ+SUFFIX
	
	
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long cfnId;
	
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(cfnId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigFormNumber)) {
            return false;
        }
        ConfigFormNumber that = (ConfigFormNumber) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(cfnId, that.cfnId);
        return eb.isEquals();
    }
    
	@Column(name = "DESCRIPTION", length = 100, nullable = false)
    private String description;
	
    @Column(name = "PREFIX", length = 45, nullable = false)
    private String prefix;
    
    @Column(name = "MIDFIX", length = 45)
    private String midfix;
    
    @Column(name = "SUFFIX", length = 45)
    private String suffix;

    @Column(name = "SEQ_LENGTH", nullable = false)
    private int seqLength;
    
    @Column(name = "CURR_SEQ", nullable = false)
    private int currSeq;
    
    @Column(name = "MID_SEQ_RESET", nullable = false)
    private Boolean midSeqReset;
    
    @Column(name = "CURR_MID", length = 45)
    private String currMid;
      
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private ConfigFormType configFormType;
    
    public String generateFormNumber() {
    	String formNumber="";
    	String prefix = (this.prefix!=null)?this.prefix:"";
		String midfix = (this.midfix!=null)?this.midfix:"";
		String suffix = (this.suffix!=null)?this.suffix:"";
		int seqLength = this.seqLength;
//		int currSeq = this.currSeq;
		int nextSeq = 0;
		String dateFormat = "";
		String paddingSeq = "";
		
		
		if (midfix.startsWith("@")) {
			dateFormat = midfix.substring(1);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			midfix = sdf.format(new Date());
			
			if (this.midSeqReset) {
				if ((currMid==null) || (!this.currMid.equals(midfix))) {
					this.currSeq = 0;
				}
			}
		}
		this.currMid = midfix;
		nextSeq = this.currSeq+1;
		this.currSeq = nextSeq;
		String pattern = "%0"+String.valueOf(seqLength)+"d";
		paddingSeq = String.format(pattern, nextSeq);
		formNumber = prefix + midfix + paddingSeq + suffix;
		return formNumber;
    }
}
