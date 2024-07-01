package com.miniplm.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.miniplm.entity.ConfigListNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableResultResponse<T> implements Serializable  {
	private List<T> data;
	private long total;
	private Boolean success;
	private String message;
	
	public TableResultResponse(List<T> allData) {
		data = allData;
		total = data.size();
		success = true;
	}
	public TableResultResponse(Set<T> allData) {
		data = new ArrayList<T>(allData);
		total = data.size();
		success = true;
	}
	
	public TableResultResponse(Page<T> allData) {
		
		data = allData.getContent();
		total = allData.getTotalElements();
		success = true;
	}
	
	public TableResultResponse(T oneData) {
		data = new ArrayList<T>();
		data.add(oneData);
		total = 1;
		success = true;
	}
	public TableResultResponse(String errorMsg) {
		data = null;
		total = 0;
		success = false;
		message = errorMsg;
	}
}
