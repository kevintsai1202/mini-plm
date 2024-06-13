package com.miniplm.response;

import java.util.HashMap;
import java.util.Map;

import com.miniplm.entity.ConfigFormField;

import lombok.Data;

@Data
public class GroupEnum {
	private Map<String, String> enumData = new HashMap<>();
	
	public GroupEnum(ConfigFormField cff) {
		enumData.put(cff.getDataIndex(), cff.getFieldName());
	}
}
