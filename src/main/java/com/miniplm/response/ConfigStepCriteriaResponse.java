package com.miniplm.response;

import java.util.List;

import com.miniplm.entity.ConfigStepCriteria;

import lombok.Data;

@Data
public class ConfigStepCriteriaResponse {
	private Long cscId;
	private Long csId;
	private Long cnId;
	private Long cfId;
	private Integer orderBy;
	private List<String> approvers;
	private List<String> notifiers;
	private List<String> requiredFields;
	
	public ConfigStepCriteriaResponse(ConfigStepCriteria newData) {
		this.cscId = newData.getCscId();
		this.cnId = newData.getCCriteriaNode().getCnId();
		this.cfId = newData.getCCriteriaNode().getConfigFormType().getCfId();
		this.csId = newData.getCStep().getCsId();
		this.orderBy = newData.getOrderBy();
		this.approvers = newData.getApprovers();
		this.notifiers = newData.getNotifiers();
		this.requiredFields =  newData.getRequiredFields();
	}
}
