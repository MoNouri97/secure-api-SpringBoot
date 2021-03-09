package com.example.secureapi.security;

import static com.example.secureapi.security.ApplicationUserPermission.STUDENT_READ;
import static com.example.secureapi.security.ApplicationUserPermission.STUDENT_WRITE;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum ApplicationUserRole {

	STUDENT(Sets.newHashSet()),
	ADMIN(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE)),
	ADMIN_TRAINEE(Sets.newHashSet(STUDENT_READ));

	private final Set<ApplicationUserPermission> permissions;

	private ApplicationUserRole(final Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}

	public Set<ApplicationUserPermission> getPermissions() {
		return permissions;
	}

	/**
	 * create a list of authorities which is permissions + roles
	 * 
	 * @return ex : [ student;read , student;write , ROLE_ADMIN ]
	 */
	public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
		final Set<SimpleGrantedAuthority> simpleAuthorities =
				permissions	.stream()
										.map(p -> new SimpleGrantedAuthority(p.getPermission()))
										.collect(Collectors.toSet());
		simpleAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		return simpleAuthorities;
	}
}
