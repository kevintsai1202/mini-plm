package com.miniplm.response;

import java.util.LinkedList;

import javax.persistence.Convert;

import com.miniplm.convert.ConverterListJson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigStepResponse {
	private Long csId;

	private String stepName;
	
	private String description;
    
    @Convert(converter = ConverterListJson.class)
    private LinkedList<Object> approvers = new LinkedList<>();
    
    @Convert(converter = ConverterListJson.class)
    private LinkedList<Object> notifiers = new LinkedList<>();
    
    private Long rejectStepId;
    
    private int orderBy;
}
