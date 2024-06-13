package com.miniplm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MP_SYSTEM_SETTING")
@Entity
public class SystemSetting {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MP_SEQUENCE_GENERATOR")
	@Column(name = "ID", unique = true, nullable = false)
	private Long sId;

	@Column(unique = true)
	private String name;

	private String value;
}
