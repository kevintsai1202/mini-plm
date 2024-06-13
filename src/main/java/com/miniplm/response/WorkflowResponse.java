package com.miniplm.response;

import java.io.Serializable;
import java.util.List;

import com.miniplm.entity.ConfigStep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkflowResponse implements Serializable {

	private Integer current;
	private String status;
	private List<ConfigStep> steps;
}

