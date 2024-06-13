package com.miniplm.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
	
	public static String setJwtCookie1(HttpServletResponse response, String token) {

		// 建立一個Cookie 物件
		Cookie cookie = new Cookie("jwt", token);

		// 設置過期時間，若無設置時間，其生命週期將持續到Session 過期為止
		cookie.setMaxAge(2*7*24*60*60);

		// 設置為不能被JS 訪問的Cookie
		cookie.setHttpOnly(true);

		cookie.setPath("/");
		// 將Cookie 物件加入Response 中
		response.addCookie(cookie);
		
		return "add jwt Cookie";
	}
	
	public static String getJwtCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie: cookies) {
			if (cookie.getName().equals("jwt")) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
	public static String deleteJwtCookie(HttpServletResponse response) {

		// 將Cookie 值設置為null
		Cookie cookie = new Cookie("jwt", null);

		// 設置過期時間為0
		cookie.setMaxAge(0);

		// 將Cookie 物件加入Response 中
		response.addCookie(cookie);
		
		return "delete jwt Cookie";
	}
}
