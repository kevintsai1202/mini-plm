package com.miniplm.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MpUnauthorizedHandler implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.error("MpUnauthorizedHandler");
//		System.out.println("MpUnauthorizedHandler");
		authException.printStackTrace();
		response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        Map<String, String> errMap = new HashMap<String, String>();
    	errMap.put("error", "尚未登入禁止訪問");
    	
    	ObjectMapper objMap = new ObjectMapper();
//    	ResponseEntity responseEntity = new ResponseEntity<>(errMap, HttpStatus.FORBIDDEN);
//		response.setStatus(HttpStatus.UNAUTHORIZED.value());
    	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
		objMap.writeValue(response.getWriter(), errMap);
//        response.getWriter().println(responseEntity.getBody());
//        response.getWriter().flush();
	}

}
