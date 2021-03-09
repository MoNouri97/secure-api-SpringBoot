package com.example.secureapi.security;

import static com.example.secureapi.security.ApplicationUserPermission.STUDENT_WRITE;
import static com.example.secureapi.security.ApplicationUserRole.ADMIN;
import static com.example.secureapi.security.ApplicationUserRole.ADMIN_TRAINEE;

import com.example.secureapi.jwt.JwtAuthFilter;
import com.example.secureapi.jwt.JwtConfig;
import com.example.secureapi.jwt.JwtSecret;
import com.example.secureapi.jwt.JwtVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtConfig jwtConfig;
	private final JwtSecret jwtSecret;
	private final UserDetailsService userDetailsService;

	@Autowired
	public ApplicationSecurityConfig(
			// PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsService,
			JwtConfig jwtConfig,
			JwtSecret jwtSecret
	)
	{
		this.userDetailsService = userDetailsService;
		// this.passwordEncoder = passwordEncoder;
		this.jwtConfig = jwtConfig;
		this.jwtSecret = jwtSecret;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf()
				.disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(new JwtAuthFilter(authenticationManager(), jwtConfig, jwtSecret))
				.addFilterAfter(new JwtVerifier(jwtConfig, jwtSecret), JwtAuthFilter.class)
				.authorizeRequests()
				.antMatchers("/", "/login", "/css/*", "/js/*")
				.permitAll() // anyone can access
				.antMatchers("/api/**") // users with role
				.hasRole(ADMIN.name()) // ADMIN
				.antMatchers(HttpMethod.POST, "/api/**")
				.hasAuthority(STUDENT_WRITE.name())
				.antMatchers(HttpMethod.DELETE, "/api/**")
				.hasAuthority(STUDENT_WRITE.name())
				.antMatchers(HttpMethod.PUT, "/api/**")
				.hasAuthority(STUDENT_WRITE.name())
				.antMatchers(HttpMethod.GET, "/api/**")
				.hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name())
				.anyRequest()
				.authenticated();
		// .and().httpBasic();// any
		// logged
		// in
		// user
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

}
