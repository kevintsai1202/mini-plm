package com.miniplm.entity;

public enum LanguageEnum {
	zh_CHT("zh-CHT"), zh_CHS("zh-CHS"), en_US("en-US");
	
	private final String value;
	LanguageEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return this.value;
    }
}
