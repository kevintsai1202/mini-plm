package com.miniplm.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.formprops.MultipleProps;
import com.miniplm.formprops.Rules;

import lombok.Data;

@Data
public class ConfigFormTypeResponse implements Serializable{
	private Long cfId;
	private String name;
	private String description;
	
	
	public ConfigFormTypeResponse(ConfigFormType cft) {
//		
		
//		this.title = cff.getFieldName();
//		this.dataIndex = cff.getDataIndex();
//		this.valueType = cff.getFieldType();
//		this.orderBy = cff.getOrderBy();
//		if (cff.getRequired() || (cff.getPattern() != null)) {
//			this.formItemProps = new Rules(cff);
//		}
//		if (cff.getMultiple()) {
//			this.fieldProps.put("mode", "multiple");
//		}
//		if((cff.getConfigListNode() != null)) {
//			ConfigListNode listNode = cff.getConfigListNode();
//			List<ConfigListItem> listItems = listNode.getListItems();
//			Map listItemMap = new LinkedHashMap(); 
//			for(ConfigListItem listItem : listItems) {
//				Map listValue = new LinkedHashMap();
//				listValue.put("text", listItem.getValue());
//				listItemMap.put(listItem.getKey(), listValue);
//			}
//			this.valueEnum = listItemMap;
//		}
	}
}
