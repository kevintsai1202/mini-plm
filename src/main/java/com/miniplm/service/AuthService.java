package com.miniplm.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

import javax.annotation.Resource;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miniplm.entity.LdapUser;
import com.miniplm.entity.Token;
import com.miniplm.entity.YmlData;
import com.miniplm.entity.ZAccount;
import com.miniplm.repository.TokenRepository;
import com.miniplm.repository.UserRepository;
import com.miniplm.repository.ZAccountRepository;
import com.miniplm.request.AuthRequest;
import com.miniplm.response.TokenResponse;
import com.miniplm.utils.JwtUtils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final HttpServletRequest httpRequest;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtUtils jwtUtils;
	private final UserService userService;
	private final YmlData ymlData;
	private final LdapService ldapService;
	private final ZAccountRepository zaccountRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public TokenResponse authenticate(AuthRequest request)
			throws UsernameNotFoundException, AuthenticationException, UserPrincipalNotFoundException {
		String authenticateMethod = ymlData.getAuthenticateMethod();
		LdapUser ldapUser = null;
		String requestMd5Pwd = "";
		ZAccount zuser = null;

		if ("ldap".equalsIgnoreCase(authenticateMethod)) {
			ldapUser = ldapService.loginByLdap(request);
			log.info("ldapUser: {}", ldapUser);
			zuser = (ZAccount) userService.loadUserByUsername(ldapUser.getLoginName());
		} else { // db
			zuser = (ZAccount) userService.loadUserByUsername(request.getUsername());
			requestMd5Pwd = passwordEncoder.encode(request.getPassword());
			if (!zuser.getPassword().equals(requestMd5Pwd)) {
				throw new AuthenticationException("密碼錯誤");
			}
		}
		if ("ldap".equalsIgnoreCase(authenticateMethod) && (zuser.getEmail() == null)) {
			log.info("Sync email: {}", ldapUser.getMail());
			zuser.setEmail(ldapUser.getMail());
			zuser = zaccountRepository.save(zuser);
		}

		String token = jwtUtils.createToken(zuser.getUsername());
//		String refreshToken = jwtUtils.createRefreshToken(zuser.getUsername());
		log.info("token: {}", token);
		
		tokenRepository.save(Token.builder()
				.token(token)
				.expired(false)
				.revoked(false)
				.user(zuser)
				.build());
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				zuser, null, zuser.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
		SecurityContextHolder.getContext().setAuthentication(authentication);
//		log.info("refresh token: {}", refreshToken);
//		LoginResponse loginResponse = LoginResponse.builder()
//				.accessToken(accessToken)
//				.refreshToken(refreshToken)
//				.build();
		return new TokenResponse(token, zuser);
	}
}