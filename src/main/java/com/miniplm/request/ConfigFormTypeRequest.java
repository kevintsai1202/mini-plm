package com.miniplm.request;

import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class ConfigFormTypeRequest {
	private String name;
	private String description;
	private List<Long> cfnIds;
	private Set<Long> ctIds;
	private Long cwId;
}
