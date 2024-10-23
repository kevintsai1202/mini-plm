package com.miniplm.listener;

import javax.persistence.PostLoad;

import org.springframework.stereotype.Component;

import com.miniplm.entity.FormData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FormDataListener {
	
	@PostLoad
    public void postLoad(FormData formData) {
        // 在实体加载时缓存旧的实体状态
		formData.setOldData(new FormData(formData));
    }
}
