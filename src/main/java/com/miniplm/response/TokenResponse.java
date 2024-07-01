package com.miniplm.response;

import com.miniplm.entity.ZAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class TokenResponse {
	private String token;
	private ZAccount loginUser;
}
