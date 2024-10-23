package com.miniplm.request;

import lombok.Data;

@Data
public class ConfigWorkflowRequest {
	private String name;
	private String description;
	private Boolean status=false;
	private Long cfId;
}
