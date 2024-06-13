package com.miniplm.entity;

import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MP_TOKEN")
@Entity
@ToString(exclude = {"user"})
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MP_SEQUENCE_GENERATOR")
	@Column(name = "ID", unique = true, nullable = false)
	private Long tId;

	@Column(unique = true)
	private String token;

	private boolean revoked;

	private boolean expired;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", referencedColumnName = "ACCOUNT_ID")
	private ZAccount user;

	@CreationTimestamp
	@JsonIgnore
	private Instant createTime;

	@UpdateTimestamp
	@JsonIgnore
	private Instant updateTime;
}
