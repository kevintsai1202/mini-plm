package com.miniplm.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.AntPathMatcher;

import com.miniplm.entity.Permission;
import com.miniplm.security.JwtAuthenticationFilter;
import com.miniplm.security.MpAccessDeniedHandler;
import com.miniplm.security.MpAuthenticationFailureHandler;
import com.miniplm.security.MpAuthenticationProvider;
import com.miniplm.security.MpUnauthorizedHandler;
import com.miniplm.service.AuthorizationService;
import com.miniplm.service.UserService;
import com.miniplm.utils.Md5PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

	private final MpUnauthorizedHandler unauthorizedHandler;
	private final MpAccessDeniedHandler accessDeniedHandler;
	private final AuthorizationService authorizationService;
	private final LogoutHandler logoutHandler;
    private final JwtAuthenticationFilter jwtAuthenticationTokenFilter;
	
//	@Bean
//	public RememberMeServices persistentTokenBasedRememberMeServices() {
//	    return new PersistentTokenBasedRememberMeServices("remember-me", userService, persistentTokenRepository());
//	}

//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//        tokenRepository.setDataSource(dataSource); // 设置数据源
////        tokenRepository.setCreateTableOnStartup(true); // 启动创建表，创建成功后注释掉
//        return tokenRepository;
//    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Md5PasswordEncoder();
	}

//	@Bean
//	public PasswordEncoder testPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

	@Bean
	public MpAuthenticationProvider jwtAuthenticationProvider() {
		return new MpAuthenticationProvider();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

//	@Bean
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
////		auth.ldapAuthentication()
//	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.cors(withDefaults())
				.csrf(csrf -> csrf.disable())
				.formLogin(login -> login.loginPage("/login")
										 .failureHandler(new MpAuthenticationFailureHandler())
										 .permitAll()
						)
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.headers(header -> header.cacheControl())
				.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(	e -> e
						.authenticationEntryPoint(unauthorizedHandler)
						.accessDeniedHandler(accessDeniedHandler)
						)
				.authorizeHttpRequests(requests -> requests
						.antMatchers("/").permitAll()
						.antMatchers("/h2-console/**").permitAll()
						.antMatchers("/v3/**").permitAll()
						.antMatchers("/swagger-ui/**").permitAll()
						.antMatchers("/swagger-resources/**").permitAll()
						.antMatchers("/static/**").permitAll()
						.antMatchers("/react/**").permitAll()
						.antMatchers("/user/**").permitAll()
						.antMatchers("/assets/**").permitAll()
						.antMatchers("/favicon.ico").permitAll()
						.antMatchers("/login").permitAll()
						.antMatchers("/index.html").permitAll()
						.antMatchers("/api/v1/config/systemsetting/routes").permitAll()					
						.antMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
						.antMatchers(HttpMethod.POST, "/api/v1/users/**").authenticated()
						.antMatchers(HttpMethod.POST, "/api/v1/seatch/**").permitAll()
						.antMatchers(HttpMethod.PUT, "/api/v1/forms/formnumber/**").permitAll()	//外部修改呼叫
						.antMatchers("/api/v1/files/**").permitAll()
						.antMatchers("/api/v1/config/authorization/me/**").permitAll()
						.antMatchers("/api/v1/config/authorization/me/**")
						.access((authenticationSupplier, requestAuthorizationContext) -> {
//									// 當前用戶角色
							Collection<? extends GrantedAuthority> authorities = authenticationSupplier.get()
									.getAuthorities();
							Object principal = authenticationSupplier.get().getPrincipal();
							Set<Permission> permissions = new HashSet<>();
							if (principal instanceof UserDetails) {
								UserDetails user = (UserDetails) principal;
								permissions = authorizationService.getUserPermissions(user.getUsername());
							}
							String requestUri = requestAuthorizationContext.getRequest().getRequestURI();
							String method = requestAuthorizationContext.getRequest().getMethod();
							AntPathMatcher antMatcher = new AntPathMatcher();
							for(Permission permission : permissions) {
								if ((method.equals(permission.getMethod())) && (antMatcher.match(permission.getUriPattern(), requestUri))) 
									return new AuthorizationDecision(true);
							}
							return new AuthorizationDecision(false);
						}))
//				.rememberMe(rem -> {
//						 rem.alwaysRemember(true)
//							.rememberMeServices(persistentTokenBasedRememberMeServices())
//							.tokenValiditySeconds(2*7*24*60*60);
//						}
//				)
				.logout(logout ->
                	logout.logoutUrl("/api/v1/auth/logout")
                          .addLogoutHandler(logoutHandler)
                          .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//                          .deleteCookies("remember-me")
						);
		return httpSecurity.build();
	}
}
