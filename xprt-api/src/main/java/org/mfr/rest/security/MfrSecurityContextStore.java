package org.mfr.rest.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mfr.data.Useracc;
import org.mfr.data.XSession;
import org.mfr.xprt.data.XSessionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;


@Component
public class MfrSecurityContextStore implements 
		SecurityContextRepository {
	private static Logger log = LoggerFactory.getLogger(MfrSecurityContextStore.class);
	@Autowired
	private XSessionDao xSessionDao;
	
	@Override
	public SecurityContext loadContext(
			HttpRequestResponseHolder requestResponseHolder) {
		HttpServletRequest req=requestResponseHolder.getRequest();
		String authorization=req.getHeader("Authorization");
		if(authorization!=null && authorization.length()>6){
			String token=authorization.substring(7);
			XSession session=xSessionDao.getSessionByAuthToken(token);
			if(session!=null){
				List<GrantedAuthority> grantedList=new ArrayList<GrantedAuthority>(session.getScopes().length);
				for (int i = 0; i < session.getScopes().length; i++) {
					log.debug("build user grant list:"+session.getScopes()[i]);
					grantedList.add(new SimpleGrantedAuthority(session.getScopes()[i]));
				}
				UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(session.getUseracc(),null, grantedList);
				SecurityContextHolder.getContext().setAuthentication(auth);
				log.debug("setAuthenctication for context");
				return SecurityContextHolder.getContext();
			}
		}
		return SecurityContextHolder.getContext();
	}
	public static Authentication createStaticAuthenticationObject(Useracc user){
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new UsernamePasswordAuthenticationToken(user,null, grantedAuths);
	}
	@Override
	public void saveContext(SecurityContext context,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO cache token		
//		Cache sessionCache=CacheHandler.getCache(TOKENCACHE);
//		sessionCache.put(element);
//		new Element(token,session);
		//SecurityContextHolder.getContext().getAuthentication()
		
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		
		return false;
	}
	
}
