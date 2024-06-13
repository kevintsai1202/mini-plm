package com.miniplm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UriEntity {
	int index;
	String name;
	String httpMethod;
	String uri;
}
