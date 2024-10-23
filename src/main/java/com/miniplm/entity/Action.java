package com.miniplm.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Proxy(lazy = false)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "MP_ACTION")
@SQLDelete(sql = "UPDATE MP_ACTION SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
public class Action extends BaseEntity{
	

//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	private Long aId;
	
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(aId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Action)) {
            return false;
        }
        Action that = (Action) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(aId, that.aId);
        return eb.isEquals();
    }
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FORM_ID", referencedColumnName = "ID")
	private Form form;
	
	@OneToOne
	@JoinColumn(name = "CONFIG_STEP_ID", referencedColumnName = "ID")
	private ConfigStep configStep;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ACCOUNT_ID")
	private ZAccount user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SIGNOFF_ID", referencedColumnName = "ACCOUNT_ID")
	private ZAccount signoffUser;
    
    //A:approve N:Notify C:Comment
    @Column(name = "TYPE", length = 1)
    private String type;
    
    //true:approval false:reject
    @Column(name = "SIGN_OFF_TYPE")
    private Boolean signoffType;
    
    @Column(name = "COMMENTS", length = 2000)
    private String comments;
    
    @Column(name = "CAN_NOTICE_FLAG")
    private Boolean canNoticeFlag = false;
    
    @Column(name = "NOTICE_FLAG")
    private Boolean noticeFlag = false;
    
    @Column(name = "FINISH_FLAG")
    private Boolean finishFlag = false;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CREATOR_ID", referencedColumnName = "ACCOUNT_ID")
    private ZAccount creator;

	@Override
	public String toString() {
		return "Action [form=" + form.getFormNumber() + ", configStep=" + configStep.getStepName() + ", user=" + user.getUsername() + ", type=" + type
				+ ", finishFlag=" + finishFlag + "]";
	}
    
}
