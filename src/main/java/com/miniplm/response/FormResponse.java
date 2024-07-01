package com.miniplm.response;

import java.io.Serializable;
import java.time.Instant;

import com.miniplm.entity.Form;
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
	private Instant createTime;
//	private List<FormExtraData> formExtraDatas;
	public FormResponse(Form form){
		String workflowName = "";
		String stepName = "";
		String creatorId = "";
		if (form.getCWorkflow() != null) {
			workflowName = form.getCWorkflow().getName();
			stepName = form.getCurrStep().getStepName();
		}
		if (form.getCreator() != null) {
			creatorId = form.getCreator().getId();
		}
		
		this.fId = form.getFId();
		this.formNumber = form.getFormNumber();
		this.formTypeId = form.getConfigFormType().getCfId();
		this.formTypeName =	form.getConfigFormType().getName();
		this.description = form.getDescription(); 
		this.workflowName =	workflowName; 
		this.stepName = stepName; 
		this.creatorId = creatorId;
		this.createTime = form.getCreateTime();
	}
	
}
