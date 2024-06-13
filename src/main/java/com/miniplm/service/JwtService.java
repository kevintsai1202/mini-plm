package com.miniplm.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.security.auth.login.CredentialNotFoundException;
import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miniplm.entity.LdapUser;
import com.miniplm.entity.Token;
import com.miniplm.entity.YmlData;
import com.miniplm.entity.ZAccount;
import com.miniplm.repository.TokenRepository;
import com.miniplm.repository.ZAccountRepository;
import com.miniplm.request.AuthRequest;
import com.miniplm.response.TokenResponse;
import com.miniplm.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@ConfigurationProperties(prefix = "jwt")
@Service
@Data
@Slf4j
@RequiredArgsConstructor
public class JwtService {
//    private String secretKey;
//    private int lifeTime;

	private final JwtUtils jwtUtils;

	private final AuthenticationManager authenticationManager;

	private final UserService userService;

	private final YmlData ymlData;

	private final LdapService ldapService;

	private final ZAccountRepository zaccountRepository;

	private final TokenRepository tokenRepository;

//	private final PasswordEncoder passwordEncoder;
	
//	  public String extractUsername(String token) {
//		  Claims claims = jwtUtils.extractAllClaims(token);
//		  return (String) claims.get("username");
//	  }
	
	@Transactional
	public String createToken(String username) {
		ZAccount user = (ZAccount) userService.loadUserByUsername(username);
		String token = jwtUtils.createToken(username);
//		TokenResponse res = new TokenResponse();
//		res.setToken(token);
		
		tokenRepository.disableTokenByUser(user.getId());	//將該user的Token都設為失效
		
		tokenRepository.save(Token.builder()
				.token(token)
				.user(user)
				.expired(false)
				.revoked(false)
				.build());
		return token;
	}
	
	@Transactional
	public String refresfToken(String token) throws CredentialNotFoundException {
		Map claims = jwtUtils.parseToken(token);
		String username = (String) claims.get("username");
		ZAccount user = (ZAccount) userService.loadUserByUsername(username);
		Optional<Token> opToken = tokenRepository.findByToken(token);
		if (opToken.isPresent()) {
			Token dbToken = opToken.get();
			dbToken.setExpired(true);
			tokenRepository.save(dbToken);
		}else {
			throw new CredentialNotFoundException();
		}
		
		String refreshToken = jwtUtils.createToken(username);
		

		
		tokenRepository.disableTokenByUser(user.getId());
		
		tokenRepository.save(Token.builder()
				.token(token)
				.user(user)
				.expired(false)
				.revoked(false)
				.build());
		
		return refreshToken;
	}

//	public String generateToken1(ZAccount zuser) {
//		String token = jwtUtils.generateToken(zuser);
//		log.info("new token: {}" , token);
////		System.out.println("reflash token:" + token);
//		Optional<Token> optToken = tokenRepository.findByToken(token);
//		if (!optToken.isPresent()) {
//			tokenRepository.save(Token.builder().token(token).expired(false).revoked(false).user(zuser).build());
//		}
////			dbToken = optToken.get();
//		return token;
//	}

//	public String generateToken(AuthRequest auth)
//			throws UserPrincipalNotFoundException, NoSuchAlgorithmException, AuthenticationException {
//		String authenticateMethod = ymlData.getAuthenticateMethod();
//		LdapUser ldapUser = null;
//		String requestMd5Pwd = "";
//		ZAccount zuser = null;
//		try {
//			if ("ldap".equalsIgnoreCase(authenticateMethod)) {
//				ldapUser = ldapService.loginByLdap(auth);
//				log.info("ldapUser: {}" , ldapUser);
////				System.out.println("ldapUser:" + ldapUser);
//				zuser = (ZAccount) userService.loadUserByUsername(ldapUser.getLoginName());
//			} else { // db
//				zuser = (ZAccount) userService.loadUserByUsername(auth.getUsername());
//			}
//
//			log.info("zuser: {}" , zuser);
//			requestMd5Pwd = passwordEncoder.encode(auth.getPassword());
//			if ("db".equalsIgnoreCase(authenticateMethod) && (!zuser.getPassword().equals(requestMd5Pwd))) {
//				throw new AuthenticationException("密碼錯誤");
//			}
//
//			if ("ldap".equalsIgnoreCase(authenticateMethod) && (zuser.getEmail() == null)) {
//				log.info("Sync email: {}", ldapUser.getMail());
////				System.out.println("Sync email");
//				zuser.setEmail(ldapUser.getMail());
//				zuser = zaccountRepository.save(zuser);
//			}
//		} catch (UsernameNotFoundException e) {
//			log.info("Create zaccount");
////			System.out.println("Create zaccount");
//			zuser = new ZAccount();
//			zuser.setUsername(auth.getUsername());
////			zuser.setPassword(requestMd5Pwd);
//			zuser.setEmail(ldapUser.getMail());
//			zuser.setDeleteFlag("N");
//			zuser = zaccountRepository.save(zuser);
//		}
//		
//		String token = jwtUtils.createToken(zuser.getUsername());
//		log.info("token: {}" , token);
////		System.out.println("token:" + token);
////		Token dbToken = null;
//		Optional<Token> optToken = tokenRepository.findByToken(token);
//		if (!optToken.isPresent()) {
////			dbToken = optToken.get();
//			tokenRepository.save(Token.builder().token(accessToken).expired(false).revoked(false).user(zuser).build());
//		}
//		return accessToken;
//	}

//	public String generateToken(MultiValueMap<String, String> formData) throws UserPrincipalNotFoundException, NoSuchAlgorithmException, AuthenticationException {
//		String authenticateMethod = ymlData.getAuthenticateMethod();
//		LdapUser ldapUser = null;
//		String requestMd5Pwd = "";
//		ZAccount zuser = null;
//		try {
//			if ("ldap".equalsIgnoreCase(authenticateMethod)) {
//				ldapUser = ldapService.loginByLdap(formData);
//				System.out.println("ldapUser:"+ldapUser);
//				zuser = (ZAccount) userService.loadUserByUsername(ldapUser.getLoginName());
//			}else { //db
//				zuser = (ZAccount) userService.loadUserByUsername(formData.get("username").toString());
//			}
//
//			System.out.println("zuser: "+zuser);
//			System.out.println("zaccount user:"+zuser.getUsername());
//			System.out.println("zaccount pwd:"+zuser.getPassword());
//			requestMd5Pwd = passwordEncoder.encode(formData.get("password").toString());
//			System.out.println("login pwd:"+ requestMd5Pwd);
//			if ("db".equalsIgnoreCase(authenticateMethod) && (!zuser.getPassword().equals(requestMd5Pwd))) {
//				throw new AuthenticationException("密碼錯誤");
//			}
//			
//			if ("ldap".equalsIgnoreCase(authenticateMethod) && ((zuser.getPassword()==null) || (!zuser.getPassword().equals(requestMd5Pwd)))) {
//				System.out.println("Sync password");
//				zuser.setPassword(requestMd5Pwd);
//				zuser = zaccountRepository.save(zuser);
//			}
//			if ("ldap".equalsIgnoreCase(authenticateMethod) && (zuser.getEmail() == null)) {
//				System.out.println("Sync email");
//				zuser.setEmail(ldapUser.getMail());
//				zuser = zaccountRepository.save(zuser);
//			}
//		}catch (UsernameNotFoundException e) {
//			System.out.println("Create zaccount");
//			zuser = new ZAccount();
//			zuser.setUsername(formData.get("username").toString());
//			zuser.setPassword(requestMd5Pwd);
//			zuser.setEmail(ldapUser.getMail());
//			zuser.setDeleteFlag("N");
//			zuser = zaccountRepository.save(zuser);
//		}
//		
//        Authentication authentication =
//                new UsernamePasswordAuthenticationToken(formData.get("username").toString(), formData.get("password").toString());
//        authentication = authenticationManager.authenticate(authentication);
//        String token = jwtUtils.generateToken(zuser);
//        
//        tokenRepository.save(
//        		Token.builder()
//        		.token(token)
//        		.expired(false)
//        		.revoked(false)
//        		.user(zuser)
//        		.build()
//        		);
//        return token;
//    }

}
