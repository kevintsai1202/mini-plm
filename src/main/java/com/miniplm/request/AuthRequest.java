package com.miniplm.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {
	@NotBlank
    private String username;
	@NotBlank
    private String password;
	@NotBlank
    private String rememberMe;
}
