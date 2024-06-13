package com.miniplm.request;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ConfigCriteriaNodeRequest {
	@NotBlank
	private Long cnId;
	@NotBlank
	private String criteriaName;
	
	private Long cfId;
	
}
