package com.miniplm.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.formprops.Rules;
import com.miniplm.service.AuthorizationService;

import lombok.Data;

@Data
public class UserFormFieldResponse implements Serializable{
	private String title;
	private String dataIndex;
	private String valueType;
	private Boolean collapsible;
	private Rules formItemProps;
	private Map fieldProps = new HashMap();
	private int orderBy;
	private Map valueEnum;
	private List columns = new ArrayList();
	
	public UserFormFieldResponse(String groupName, String dataIndex) {
		this.title = groupName;
		this.dataIndex = dataIndex;
		this.valueType = "group";
		this.fieldProps.put("collapsible", true);
		this.fieldProps.put("style", 
				new HashMap(){{
			put("backgroundColor","#f0f7ff");
		}});
		this.fieldProps.put("titleStyle", 
				new HashMap(){{
					put("backgroundColor","#7ebaff");
			}});
	}
	
	public UserFormFieldResponse(ConfigFormField cff, boolean stepRequired) {
		this.title = cff.getFieldName();
		this.dataIndex = cff.getDataIndex();
		this.valueType = cff.getFieldType().equals("multilist")?"select":cff.getFieldType();
		this.orderBy = cff.getOrderBy();
		
		
		if (stepRequired || cff.getRequired() || (cff.getPattern() != null)) {
			this.formItemProps = new Rules(cff, stepRequired);
		}
		
		if (cff.getMultiple()) {
			this.fieldProps.put("mode", "multiple");
		}
		if((cff.getConfigListNode() != null)) {
			ConfigListNode listNode = cff.getConfigListNode();
			List<ConfigListItem> listItems = listNode.getListItems();
			Map listItemMap = new LinkedHashMap(); 
			for(ConfigListItem listItem : listItems) {
				Map listValue = new LinkedHashMap();
				listValue.put("text", listItem.getValue());
				listItemMap.put(listItem.getKey(), listValue);
			}
			this.valueEnum = listItemMap;
		}
	}
	
	public UserFormFieldResponse(Long formTypeId, ConfigFormField cff, boolean stepRequired, boolean canModify) {
		this.title = cff.getFieldName();
		this.dataIndex = cff.getDataIndex();
		this.valueType = cff.getFieldType().equals("multilist")?"select":cff.getFieldType();
		this.orderBy = cff.getOrderBy();
		
		
		if (stepRequired || cff.getRequired() || (cff.getPattern() != null)) {
			this.formItemProps = new Rules(cff, stepRequired);
		}
		
		//TODO 增加權限判斷
		if (canModify)
			this.fieldProps.put("disabled", false);
		else
			this.fieldProps.put("disabled", true);
		
		if (cff.getMultiple()) {
			this.fieldProps.put("mode", "multiple");
		}
		if((cff.getConfigListNode() != null)) {
			ConfigListNode listNode = cff.getConfigListNode();
			List<ConfigListItem> listItems = listNode.getListItems();
			Map listItemMap = new LinkedHashMap(); 
			for(ConfigListItem listItem : listItems) {
				Map listValue = new LinkedHashMap();
				listValue.put("text", listItem.getValue());
				listItemMap.put(listItem.getKey(), listValue);
			}
			this.valueEnum = listItemMap;
		}
	}
}
