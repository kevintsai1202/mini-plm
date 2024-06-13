package com.miniplm.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class YmlData {
	
	@Value("${fileStorage}")
	private String fileStorage;
	
	@Value("${authenticate-method}")
	private String authenticateMethod;
	
}
