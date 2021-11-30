package com.monkata.lps.security;

import java.util.Arrays;
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.monkata.lps.service.JwtUserDetailsService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());

	}


	@Autowired
	RepositoryRestConfiguration rest;

	@Autowired
	private EntityManager entityManager;

	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPointBean() throws Exception {
		return new JwtAuthenticationEntryPoint();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(
				Arrays.asList("Accept", "Origin", "Content-Type", "Depth", "User-Agent", "If-Modified-Since,","Cache-Control", "Authorization", "X-Req", "X-File-Size", "X-Requested-With", "X-File-Name","apikey","appkey"));
		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		rest.exposeIdsFor(
				entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));
		httpSecurity.csrf().disable().authorizeRequests()
				.antMatchers("/","/api/isUserNameExist/**","/api","/api/contacts","/api/getFiles/**","/public/**","/assets/logo/**","/assets/imgs/**","/assets/**", "/api/login", "/api/logout","/api/authenticate", "/api/register","/api/home","/api/fpass/**","/api/backdoor/**","/api/bank","/api/bankAndLang", "/api/lang/**", "/api/test","/api/cpass/**", "/api/newPassword","/api/moncash/addDepo/**","/api/moncash/addDepoFromWallet/**").permitAll().anyRequest()
				.authenticated().and().cors().and().exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	 
   
}