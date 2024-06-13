package com.miniplm.request;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ConfigFormFieldRequest {
	private Long cffId;
	private Long clnId;
	private String fieldName;
    private String groups;
    private String pattern;
    private String patternMsg;
    private int orderBy;
}
