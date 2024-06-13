package com.miniplm.response;

import java.io.Serializable;

import com.miniplm.entity.Action;
import com.miniplm.entity.FormData;
import com.miniplm.entity.ZAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ActionResponse implements Serializable {
	private Long aId;
	private String type;
	private String comments;
	private Boolean signoffType;
	private String stepName;
	private Long formId;
	private String formNumber;
	private String formTypeName;
	private String formDescription;
	private String username;
	private Boolean finishFlag;
//	private List<FormExtraData> formExtraDatas;
	public ActionResponse(Action action) {
		this.aId = action.getAId();
		this.type = action.getType();
		this.stepName = action.getConfigStep().getStepName();
		this.formId = action.getForm().getFId();
		this.formNumber = action.getForm().getFormNumber();
		this.formTypeName = action.getForm().getConfigFormType().getName();
		this.formDescription = action.getForm().getDescription();
		this.username = action.getUser().getUsername();
		this.finishFlag = action.getFinishFlag();
	}
}

