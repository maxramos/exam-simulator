package com.maxaramos.examsimulator.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	private OAuth2LogoutHandler oAuth2LogoutHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		OAuth2AuthorizationRequestRedirectFilter
//		OAuth2LoginAuthenticationFilter
//		OAuth2LoginAuthenticationProvider
//		OidcAuthorizationCodeAuthenticationProvider
//		DefaultLoginPageGeneratingFilter

		http
			.requiresChannel()
				.anyRequest().requiresSecure()
				.and()
			.authorizeRequests()
				.antMatchers("/login").permitAll()
				.and()
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.oauth2Login()
				.loginPage("/login");
//			.logout()
//				.addLogoutHandler(oAuth2LogoutHandler);
	}



}
