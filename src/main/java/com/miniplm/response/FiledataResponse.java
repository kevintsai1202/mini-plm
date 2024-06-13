package com.miniplm.response;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.miniplm.entity.Filedata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiledataResponse {
	private Long fId;
	private String fileName;
	private String uuid;
	private Long size;
    private String url;  
    
    public FiledataResponse(Filedata filedata) {
    	String homeURL = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    	this.fId = filedata.getFdId();
    	this.fileName = filedata.getFileName();
    	this.size = filedata.getFileSize();
    	this.url = homeURL+"/api/v1/files/uuid/"+filedata.getStorageUuid();
    	this.uuid = filedata.getStorageUuid();
    }
}
