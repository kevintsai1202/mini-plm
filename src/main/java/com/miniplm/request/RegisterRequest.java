package com.miniplm.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RegisterRequest {
	@NotBlank
    private String email;
	@NotBlank
    private String username;
	@NotBlank
    private String password;
}