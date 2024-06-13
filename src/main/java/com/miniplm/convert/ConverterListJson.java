package com.miniplm.convert;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.AttributeConverter;

import com.miniplm.utils.JsonUtils;

public class ConverterListJson implements AttributeConverter<LinkedList<Object>, String> {

	@Override
	public String convertToDatabaseColumn(LinkedList<Object> attribute) {
		return JsonUtils.toJson(attribute);
	}

	@Override
	public LinkedList<Object> convertToEntityAttribute(String dbData) {
		return JsonUtils.toObject(dbData, LinkedList.class);
	}
}
