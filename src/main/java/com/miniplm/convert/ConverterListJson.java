package com.miniplm.convert;

import java.util.List;

import javax.persistence.AttributeConverter;

import com.miniplm.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConverterListJson implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
//		log.info("From UI:{}", attribute);
		if (attribute == null || attribute.isEmpty()) {
            return "";
        }
		return JsonUtils.toJson(attribute);
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
//		log.info("From DB:{}", dbData);
		if (dbData == null || dbData.isEmpty()) {
            return null;
        }		
		return JsonUtils.toObject(dbData, List.class);
	}
}
