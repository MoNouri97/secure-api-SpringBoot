package com.example.secureapi.security;

import static com.example.secureapi.security.ApplicationUserPermission.STUDENT_WRITE;
import static com.example.secureapi.security.ApplicationUserRole.ADMIN;
import static com.example.secureapi.security.ApplicationUserRole.ADMIN_TRAINEE;
import static com.example.secureapi.security.ApplicationUserRole.STUDENT;

import com.example.secureapi.jwt.JwtConfig;
import com.example.secureapi.jwt.JwtSecret;
import com.example.secureapi.jwt.JwtAuthFilter;
import com.example.secureapi.jwt.JwtVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	private final PasswordEncoder passwordEncoder;
	private final JwtConfig jwtConfig;
	private final JwtSecret jwtSecret;

	@Autowired
	public ApplicationSecurityConfig(
			PasswordEncoder passwordEncoder,
			JwtConfig jwtConfig,
			JwtSecret jwtSecret
	)
	{
		this.passwordEncoder = passwordEncoder;
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
				// .antMatchers("/api/**") // users with role
				// .hasRole(ADMIN.name()) // ADMIN
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
	@Bean
	protected UserDetailsService userDetailsService() {
		UserDetails nouri =
				User.builder()
						.username("nouri")
						.password(passwordEncoder.encode("0000"))
						// .roles(STUDENT.name()) // ROLE_STUDENT
						.authorities(STUDENT.getGrantedAuthorities())
						.build();
		UserDetails admin =
				User.builder()
						.username("admin")
						.password(passwordEncoder.encode("0000"))
						// .roles(ADMIN.name()) // ROLE_ADMIN
						.authorities(ADMIN.getGrantedAuthorities()) // Permissions instead of roles
						.build();
		UserDetails adminTrainee =
				User.builder()
						.username("tom")
						.password(passwordEncoder.encode("0000"))
						// .roles(ADMIN_TRAINEE.name()) // ROLE_ADMIN_TRAINEE
						.authorities(ADMIN_TRAINEE.getGrantedAuthorities())
						.build();
		// also must add password encoder

		return new InMemoryUserDetailsManager(nouri, admin, adminTrainee);

	}

}
