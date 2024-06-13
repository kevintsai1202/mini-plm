package com.miniplm.response;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import com.miniplm.entity.ConfigStep;
import com.miniplm.entity.ConfigWorkflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ConfigWorkflowResponse implements Serializable {
	
	public ConfigWorkflowResponse(ConfigWorkflow workflow) {
		this.cwId = workflow.getCwId();
		this.cfId = workflow.getCFormType().getCfId();
		this.description = workflow.getDescription();
		this.name = workflow.getName();
		this.status = workflow.getStatus();
		this.cStep = workflow.getCSteps();
	}
	
	private List<ConfigStep> cStep;
	
	private Long cwId;
	
	private String name;
	
	private String description;
    
    private Boolean status;
    
    private Long cfId;
}

