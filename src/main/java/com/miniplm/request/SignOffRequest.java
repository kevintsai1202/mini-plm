package com.miniplm.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignOffRequest {
//	@NotBlank
//	private Long actionId;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private Boolean signoffType; 
	
	private String comments;
}
