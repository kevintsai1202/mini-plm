package com.miniplm.request;

import java.util.Set;

import lombok.Data;

@Data
public class ConfigStepCriteriaRequest {
	
	private Long cnId;
	private String[] approvers;
	private String[] notifiers;
	private Set<String> requiredFields;
	private Integer orderBy;
}
