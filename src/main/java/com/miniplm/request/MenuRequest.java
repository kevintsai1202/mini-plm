package com.miniplm.request;

import com.miniplm.entity.MenuEnum;

import lombok.Data;

@Data
public class MenuRequest{
	
	private Long menuId;
	
	private String key;
	
	private String label;
	
	private MenuEnum menuType;

	private String link;

	private String icon;
	
	private Long parentId;
	
	private Long configCriteriaNodeId;
	
    private int orderBy;
}
