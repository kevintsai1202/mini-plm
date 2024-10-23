package com.miniplm.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.formprops.MultipleProps;
import com.miniplm.formprops.Rules;

import lombok.Data;

@Data
public class ConfigFormFieldResponse implements Serializable{
	private String title;
	private String dataIndex;
	private String valueType;
	private Boolean collapsible;
//	private Rules formItemProps;
//	private Map fieldProps = new HashMap();
	private int orderBy;
//	private Map valueEnum;
//	private List columns = new ArrayList();
	
//	public ConfigFormFieldResponse(String groupName) {
//		this.title = groupName;
//		this.valueType = "group";
//		this.fieldProps.put("collapsible", true);
//		this.fieldProps.put("style", new HashMap(){{
//			put("backgroundColor","#f0f7ff");
//			}});
//		this.fieldProps.put("titleStyle", new HashMap(){{
//			put("backgroundColor","#7ebaff");
//			}});
//	}
	
	public ConfigFormFieldResponse(ConfigFormField cff) {
		this.title = cff.getFieldName();
		this.dataIndex = cff.getDataIndex();
		this.valueType = cff.getFieldType().toString();
		this.orderBy = cff.getOrderBy();
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
