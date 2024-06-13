package com.miniplm.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

public class MyReflectionUtils{
	public static Map<String, Object> getNonNullFields(Object obj) {
        Map<String, Object> nonNullFields = new HashMap<>();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, obj);
            if (value != null) {
                nonNullFields.put(field.getName(), value);
            }
        }

        return nonNullFields;
    }
	
	
	public static void mapNonNullFields(Object source, Object target) {
        Field[] fields = source.getClass().getDeclaredFields();
        
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, source);
            
            if (value != null) {
                String fieldName = field.getName();
                String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                Method setter = ReflectionUtils.findMethod(target.getClass(), setterName, field.getType());
                
                if (setter != null) {
                    ReflectionUtils.invokeMethod(setter, target, value);
                }
            }
        }
    }
}
