package com.miniplm.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RoleRequest {
	@NotBlank
	String roleName;
}
