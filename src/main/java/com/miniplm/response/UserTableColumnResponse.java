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
import com.miniplm.entity.ConfigTableColumn;
import com.miniplm.formprops.Rules;
import com.miniplm.service.AuthorizationService;

import lombok.Data;

@Data
public class UserTableColumnResponse implements Serializable{
	private Long id;
	private String title;
	private String dataIndex;
	private String valueType;
	private Rules formItemProps;
	private Map fieldProps = new HashMap();
	private int orderBy;
	private Map valueEnum;
//	private List columns = new ArrayList();
	
	public UserTableColumnResponse(ConfigTableColumn ctColumn) {
		this.id = ctColumn.getCtcId();
		this.title = ctColumn.getColName();
		this.dataIndex = ctColumn.getDataIndex();
		this.valueType = ctColumn.getColType().equals("multilist")?"select":ctColumn.getColType().toString();
		this.orderBy = ctColumn.getOrderBy();
		
		
		if (ctColumn.getRequired() || (ctColumn.getPattern() != null)) {
			this.formItemProps = new Rules(ctColumn);
		}
		
		if (ctColumn.getMultiple()) {
			this.fieldProps.put("mode", "multiple");
		}
		if((ctColumn.getConfigListNode() != null)) {
			ConfigListNode listNode = ctColumn.getConfigListNode();
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
	
	public UserTableColumnResponse(ConfigTableColumn ctColumn, boolean canModify) {
		this.id = ctColumn.getCtcId();
		this.title = ctColumn.getColName();
		this.dataIndex = ctColumn.getDataIndex();
		this.valueType = ctColumn.getColType().equals("multilist")?"select":ctColumn.getColType().toString();
		this.orderBy = ctColumn.getOrderBy();
		
		
		if ( ctColumn.getRequired() || (ctColumn.getPattern() != null)) {
			this.formItemProps = new Rules(ctColumn);
		}
		
		//TODO 增加權限判斷
		if (canModify)
			this.fieldProps.put("disabled", false);
		else
			this.fieldProps.put("disabled", true);
		
		if (ctColumn.getMultiple()) {
			this.fieldProps.put("mode", "multiple");
		}
		if((ctColumn.getConfigListNode() != null)) {
			ConfigListNode listNode = ctColumn.getConfigListNode();
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
