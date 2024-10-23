package com.miniplm.request;

import lombok.Data;

@Data
public class ConfigTableColumnRequest {
	private Long ctcId;
	private Long clnId;
	private String colName;
    private String pattern;
    private String patternMsg;
    private int orderBy;
}
