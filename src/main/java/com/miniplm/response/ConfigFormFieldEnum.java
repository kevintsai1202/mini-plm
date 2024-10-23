package com.miniplm.response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;

import lombok.Data;

@Data
public class ConfigFormFieldEnum {
//	private Map<Long, Map<String, String>> enumData = new HashMap<>();
	private String dataIndex;
	private String value;
	private String fieldType;
	private Map<String, String> listItemsEnum = new HashMap<>();
	
	public ConfigFormFieldEnum(ConfigFormField cff, List<ConfigListItem> fieldListItems) {
		this.dataIndex = cff.getDataIndex();
		this.value = cff.getFieldName();
		this.fieldType = cff.getFieldType().toString();
		
		if((fieldListItems != null && fieldListItems.size() > 0)) {
//			ConfigListNode listNode = cff.getConfigListNode();
//			List<ConfigListItem> fieldListItems = listNode.getListItems();
			listItemsEnum = fieldListItems.stream().collect(Collectors.toMap(ConfigListItem::getKey, ConfigListItem::getValue));
		}
//		enumData.put(cff.getCffId(), new HashMap<String, String>(){{  
//			put("text", cff.getFieldName());
//		}});
	}
	
	public ConfigFormFieldEnum(String dataIndex, String fieldName, String fieldType ) {
		this.dataIndex = dataIndex;
		this.value = fieldName;
		this.fieldType = fieldType;
		
//		enumData.put(cff.getCffId(), new HashMap<String, String>(){{  
//			put("text", cff.getFieldName());
//		}});
	}
}
