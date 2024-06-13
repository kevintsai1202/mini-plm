package com.miniplm.security;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.miniplm.entity.ZAccount;
import com.miniplm.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyAuditorAware implements AuditorAware<ZAccount> {
	@Resource
	private UserRepository userRepository;
	
	
	@Override
	public Optional<ZAccount> getCurrentAuditor() {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String userName = (String) authentication.getPrincipal();
        log.info("userName: {}",userName);
//        System.out.println("userName: "+userName);
        Optional<ZAccount> user = userRepository.findByUsername(userName);
              
        return user;
	}

}
