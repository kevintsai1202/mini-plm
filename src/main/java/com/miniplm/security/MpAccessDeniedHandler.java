package com.miniplm.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniplm.response.MessageResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MpAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.error("MpAccessDeniedHandler");
		response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        Map<String, String> errMap = new HashMap<String, String>();
    	errMap.put("error", "沒有授權禁止訪問");
//    	ResponseEntity responseEntity = new ResponseEntity<>(errMap, HttpStatus.FORBIDDEN);
//    	
//		response.setStatus(HttpStatus.FORBIDDEN.value());
//        response.getWriter().println(responseEntity.getBody());
//        response.getWriter().flush();
        
    	ObjectMapper objMap = new ObjectMapper();
//    	ResponseEntity responseEntity = new ResponseEntity<>(errMap, HttpStatus.FORBIDDEN);
		response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied");
		objMap.writeValue(response.getWriter(), errMap);
	}

}
