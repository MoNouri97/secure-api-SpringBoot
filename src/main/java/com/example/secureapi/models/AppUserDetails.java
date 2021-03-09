package com.example.secureapi.models;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserDetails implements UserDetails {

	private String username;
	private String password;
	private Set<SimpleGrantedAuthority> grantedAuthorities;

	public AppUserDetails() {
	}

	public AppUserDetails(
			String username,
			String password,
			Set<SimpleGrantedAuthority> grantedAuthorities
	)
	{
		this.username = username;
		this.password = password;
		this.grantedAuthorities = grantedAuthorities;
	}

	public AppUserDetails(Student student) {
		this.username = student.getEmail();
		this.password = student.getPassword();
		this.grantedAuthorities = student.getRole().getGrantedAuthorities();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
