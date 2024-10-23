package com.miniplm.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.PrivilegeEnum;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "System Setting api" , description = "與系統設定相關的API")
@RestController
@RequestMapping("/api/v1/config/systemsetting")
@CrossOrigin
public class SystemSettingController {
	
	@GetMapping
    public ResponseEntity<List<PrivilegeEnum>> getAllPrivilegeName() {
        return ResponseEntity.ok(Arrays.asList( PrivilegeEnum.values()));
    }
	
	
}
