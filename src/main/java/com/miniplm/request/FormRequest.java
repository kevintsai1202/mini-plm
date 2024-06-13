package com.miniplm.request;

import lombok.Data;

@Data
public class FormRequest {
	private String formNumber;
	private String description;
	private Long configFormNumberId;
}
