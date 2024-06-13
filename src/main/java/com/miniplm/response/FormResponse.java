package com.miniplm.response;

import java.io.Serializable;

import com.miniplm.entity.FormData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FormResponse implements Serializable {
	private Long fId;
	private String formNumber;
	private Long formTypeId;
	private String formTypeName;
	private String description;
	private FormData formData;
	private String workflowName;
	private String stepName;
	private String creatorId;
//	private List<FormExtraData> formExtraDatas;
}
