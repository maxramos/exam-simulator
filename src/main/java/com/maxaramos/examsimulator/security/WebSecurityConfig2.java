package com.maxaramos.examsimulator.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//@EnableWebSecurity
public class WebSecurityConfig2 {

	@Value("${spring.security.user.name}")
	private String username;

	@Value("${spring.security.user.password}")
	private String password;

	@Value("${spring.security.user.roles}")
	private String[] roles;

	@Bean
	public UserDetailsService userDetailsService() throws Exception {
		@SuppressWarnings("deprecation")
		UserBuilder userBuilder = User.withDefaultPasswordEncoder();
		InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
		userDetailsManager.createUser(userBuilder.username(username).password(password).roles(roles).build());
		return userDetailsManager;
	}

//	@Bean
//	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext, OAuth2ProtectedResourceDetails details) {
//		return new OAuth2RestTemplate(details, oauth2ClientContext);
//	}

//	@Configuration
//	@EnableOAuth2Sso
//	@EnableOAuth2Client
	public static class UiSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
//			OAuth2ClientProperties
//			AuthorizationCodeResourceDetails
//			ResourceServerProperties
//			OAuth2ClientAuthenticationProcessingFilter
			http
				.authorizeRequests()
					.antMatchers("/", "/login**").permitAll()
					.anyRequest().authenticated()
					.and()
				.httpBasic().disable()
				.logout()
					.logoutSuccessUrl("/?logout")
					.and()
				.csrf().disable();
		}

	}

}
