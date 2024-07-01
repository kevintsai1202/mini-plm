package com.miniplm.controller;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;

import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.repository.TokenRepository;
import com.miniplm.request.AuthRequest;
import com.miniplm.response.TokenResponse;
import com.miniplm.service.AuthService;
import com.miniplm.service.JwtService;
import com.miniplm.service.UserService;
import com.miniplm.utils.JwtUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Auth API", description = "與帳號驗證相關的API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
	
	private final TokenRepository tokenRepository;
	
    private final JwtService jwtService;
	
	private final AuthService authService;
	
    private final UserService userService;

    private final JwtUtils jwtUtils;
//
	@PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest auth) throws NoSuchAlgorithmException {		
//		LoginResponse loginResponse = new LoginResponse();
//		try {
			log.info("auth: {}", auth);
//			System.out.println("auth:"+auth);
//			token = jwtService.generateToken(auth);
			TokenResponse tokenRes=null;
			try {
				tokenRes = authService.authenticate(auth);
				log.info("tokenRes: {}",tokenRes);
			} catch (UsernameNotFoundException | AuthenticationException | UserPrincipalNotFoundException e) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
//			if ((rememberMe!=null) && rememberMe.equalsIgnoreCase("true")){
//				CookieUtils.setJwtCookie(response, token);
//				ZAccount zuser = (ZAccount) userService.loadUserByUsername(auth.getUsername());
//			}
			
//		} catch ( e) {
//			
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//		}
        
//        Map<String, Object> responseData = Collections.singletonMap("token", token);
        return ResponseEntity.ok(tokenRes);
    }
	
	public ResponseEntity<TokenResponse> refreshToken(@RequestHeader("authorization") String authorization){
		if (authorization == null || !authorization.startsWith("Bearer")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		String oldToken = authorization.substring(7);
		TokenResponse newTokenRes;
		try {
			newTokenRes = jwtService.refresfToken(oldToken);
		} catch (CredentialNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(newTokenRes);
	}
	
//	@PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(@RequestBody MultiValueMap<String, String> formData) throws UserPrincipalNotFoundException, NoSuchAlgorithmException {
//        
//		String token;
//		try {
//			token = jwtService.generateToken(formData);
//		} catch (AuthenticationException | UserPrincipalNotFoundException | NoSuchAlgorithmException e) {
//			
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//		}
//        
//        Map<String, Object> response = Collections.singletonMap("token", token);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/parse")
    public ResponseEntity<Map<String, Object>> parseToken(@RequestBody Map<String, String> auth) {
        String token = auth.get("token");
        if (jwtUtils.isExpired(token)) {
        	log.error("JWT Expired");
//        	throw new CredentialExpiredException("JWT Expired");
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, Object> responseData = jwtUtils.parseToken(token);

        return ResponseEntity.ok(responseData);
    }
    
    @PostMapping("/isexpired")
    public ResponseEntity<Map<String, String>> isExpired(@RequestHeader Map<String, String> header) throws CredentialExpiredException {
        String authorization = header.get("authorization");
        String token = authorization.split(" ")[1];
        log.info("token: {}", token);
//        System.out.println("token: "+ token);
        Map<String, String> responseData = null;
        if (jwtUtils.isExpired(token)) {
//        	throw new CredentialExpiredException("JWT Expired");
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    	responseData = Collections.singletonMap("message", "Toekn 尚未過期");
    	return ResponseEntity.ok(responseData);
    }
    
//	@Autowired
//	private AuthService authService;
//
//	@PostMapping
//	public ResponseEntity<?> getToken(@Valid @RequestBody AuthRequest request) {
//      return authService.getToken(request);
//	}
}
