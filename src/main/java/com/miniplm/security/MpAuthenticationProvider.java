package com.miniplm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import com.miniplm.entity.ZAccount;
import com.miniplm.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MpAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = String.valueOf(authentication.getPrincipal());
		String password = String.valueOf(authentication.getCredentials());

		log.info("username: {} password: {}", username, password);
//		System.out.println("username:"+username);
//		System.out.println("password:"+password);
		UserDetails user = userService.loadUserByUsername(username);
//		try {requestMd5Pwd = passwordEncoder.encode(request.getPassword());
//			if (DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase().equals(user.getPassword())) {
//			if( passwordEncoder.encode(password).equals(user.getPassword())) {
			if( passwordEncoder.matches(password,user.getPassword())) {
//				return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
				return new UsernamePasswordAuthenticationToken( user, null, user.getAuthorities());
			}else {
				throw new BadCredentialsException("Error!!");
			}
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new BadCredentialsException("Error!!");
//		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
