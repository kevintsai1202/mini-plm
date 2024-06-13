package com.miniplm.response;

import java.util.LinkedList;

import com.miniplm.entity.ConfigStepCriteria;

import lombok.Data;

@Data
public class ConfigStepCriteriaResponse {
	private Long cscId;
	private Long csId;
	private Long cnId;
	private Long cfId;
	private Integer orderBy;
	private LinkedList<Object> approvers;
	private LinkedList<Object> notifiers;
	private LinkedList<Object> requiredFields;
	
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