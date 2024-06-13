package com.miniplm.entity;

import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "MP_REMEMBERME")
@Entity
public class RememberMe {
	  @Id
	  @Column(name = "series", unique = true, nullable = false,length = 64)
	  private String series;

	  @NonNull
	  @Column(name = "username",length = 64,nullable = false)
	  private String token;
	  
	  @NonNull
	  @Column(name = "token",length = 64,nullable = false)
	  private String username;
	  
	  @NonNull
	  @Column(name = "last_userd",nullable = false)
	  private Date lastUsed; 
}
