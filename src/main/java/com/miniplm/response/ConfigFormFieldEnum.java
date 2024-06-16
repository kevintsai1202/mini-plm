package com.miniplm.response;

import java.util.HashMap;
import java.util.Map;

import com.miniplm.entity.ConfigFormField;

import lombok.Data;

@Data
public class ConfigFormFieldEnum {
//	private Map<Long, Map<String, String>> enumData = new HashMap<>();
	private String dataIndex;
	private String value;
	
	public ConfigFormFieldEnum(ConfigFormField cff) {
		dataIndex = cff.getDataIndex();
		value = cff.getFieldName();
		
//		enumData.put(cff.getCffId(), new HashMap<String, String>(){{  
//			put("text", cff.getFieldName());
//		}});
	}
}
