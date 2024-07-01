package com.miniplm.security;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.miniplm.entity.ZAccount;
import com.miniplm.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyAuditorAware implements AuditorAware<ZAccount> {
//	@Resource
//	private UserRepository userRepository;
	
	@Override
	public Optional<ZAccount> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof ZAccount) {
                        return ((ZAccount) authentication.getPrincipal());
                    }
                    return null;
                });
	}

}
