package org.mfr.rest.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mfr.manager.oauth.IOAuthService;

public class DummyOAuth implements IOAuthService{

	@Override
	public Integer getProviderId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProviderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void oAuthLoginRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openIDLoginRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String requestEmail(String arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestForAccessToken(String arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] requestForRefreshAndAccessToken(String arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> requestUserData(String arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void verifyOpenIDLogin(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		
	}

}
