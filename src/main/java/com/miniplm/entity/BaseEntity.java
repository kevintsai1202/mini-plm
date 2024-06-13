package com.miniplm.entity;

import java.time.Instant;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
	
	@CreationTimestamp
	@Column
	@JsonIgnore
	private Instant createTime;
	
	@UpdateTimestamp
	@Column
	@JsonIgnore
	private Instant updateTime;
	
	@Column
	@JsonIgnore
	private Boolean enabled=true;
}
