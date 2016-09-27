package org.mfr.rest.security;

import java.util.ArrayList;
import java.util.List;

import org.mfr.data.Useracc;
import org.mfr.xprt.data.UseraccDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class MfrAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private UseraccDao useraccDao;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String loginname = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		Useracc user = useraccDao.findByLoginAndPassword(loginname, password);
		if (user == null) {
			throw new BadCredentialsException("user not found");
		}
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth=new UsernamePasswordAuthenticationToken(loginname,password,grantedAuths);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
