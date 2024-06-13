package com.miniplm.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PermissionRequest {
	@NotBlank
	String permissionName;
	
	@NotBlank
	String method;
	
	@NotBlank
	String uriPattern;
}
