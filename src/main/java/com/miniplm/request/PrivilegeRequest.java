package com.miniplm.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.miniplm.entity.PrivilegeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PrivilegeRequest {
	@NotBlank
	Long objId;
	@NotBlank
	String privilegeName;
	@NotBlank
	String privilege;
	
	Set<String> fields;
}
