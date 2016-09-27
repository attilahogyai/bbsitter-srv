package org.mfr.xprt.manager.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.parser.JSONParser;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.codehaus.jackson.map.ObjectMapper;
import org.mfr.data.Useracc;
import org.mfr.data.UseraccPrefs;
import org.mfr.rest.util.Constants;
import org.mfr.rest.util.HttpHelper;
import org.mfr.xprt.data.UseraccPrefsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;



public class FacebookOAuthService implements IOAuthService {
	private static final Logger logger = LoggerFactory
			.getLogger(FacebookOAuthService.class);
	private RestTemplate restTemplate=new RestTemplate();
	private static final String OAuthAuthURL="https://www.facebook.com/dialog/oauth";
	private static final String clientID = Constants.FACEBOOK_OAUTH_CLIENTID;
	private static final String clientSecret = Constants.FACEBOOK_OAUTH_CLIENTSECRET;
	private static final String oAuthRedirectURL = Constants.FACEBOOK_OAUTH_OAUTHREDIRECTURL;

	@Autowired
	private UseraccPrefsDao prefsDao;
	
	
	String OAuthTokenURL = "https://graph.facebook.com/v2.3/oauth/access_token";

		
	
	private static List<Integer>tokens=new ArrayList<Integer>();
	private int lastCode=0;
	
	HttpClient httpClient=HttpClientBuilder.create().build();
	
	ObjectMapper mapper = new ObjectMapper();
	JSONParser jp=new JSONParser();
	MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
	
	
	@Override
	public void oAuthLoginRequest(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,String> parameters=new HashMap<String,String>();
		parameters.put("response_type", "code");
		parameters.put("client_id", clientID);
		parameters.put("redirect_uri", oAuthRedirectURL);
		parameters.put("scope", "email");
		int c=lastCode++;
		tokens.add(c);
		parameters.put("state", Integer.toString(c));
		
		String paramter=HttpHelper.encodeParameters(parameters);
		String forwardUrl=OAuthAuthURL+"?"+paramter;
		try {
			response.sendRedirect(forwardUrl);
		} catch (IOException e) {
			logger.error("error redirect",e);
		}
	}

	@Override
	public String[] requestForRefreshAndAccessToken(String authToken)
			throws Exception {

		HttpHeaders headers=new HttpHeaders();  
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		Map<String,String> parameters=new LinkedHashMap<String,String>();
		parameters.put("client_secret", clientSecret);
		parameters.put("redirect_uri", oAuthRedirectURL);
		parameters.put("client_id", clientID);
		parameters.put("code", authToken);
		
		
		String paramString=HttpHelper.encodeParameters(parameters);

		if(logger.isDebugEnabled())logger.debug("HttpEntity ["+paramString+"] "+headers);
		
		HttpEntity<String> entity = new HttpEntity<String>(paramString,headers);
		
		String response=restTemplate.postForObject(OAuthTokenURL, entity,String.class);
		logger.debug("response["+response+"]");
		
		Map<?,?> rootAsMap = mapper.readValue(response, Map.class);
		String accessToken=(String)rootAsMap.get("access_token");
		if(accessToken==null){
			logger.debug("access not found in reponse ["+rootAsMap+"]");
		}
		return new String[]{null,accessToken};
	}
	
	@Override
	public String requestForAccessToken(String refreshToken) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestEmail(String accessToken) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> requestUserData(String accessToken)
			throws Exception {
		
		
		HttpGet get=new HttpGet("https://graph.facebook.com/me?access_token="+URLEncoder.encode(accessToken,"UTF-8"));
		HttpResponse response;
		StringBuffer sb=new StringBuffer();
		try {
			response = httpClient.execute(get);
			String line=null;
			BufferedReader reader=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
		} catch (Exception e) {
			logger.error("requestUserData", e);
			throw e;
		}
		logger.debug("requestUserData result:"+sb.toString());
		Map<String,Object> data = mapper.readValue(sb.toString(), Map.class);
		
		
		get=new HttpGet("https://graph.facebook.com/"+data.get("id")+"/picture?access_token="+URLEncoder.encode(accessToken));
		sb=new StringBuffer();
		try {
			response = httpClient.execute(get);
			//String line=null;
			//BufferedReader reader=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//			while((line=reader.readLine())!=null){
//				sb.append(line);
//			}
			data.put("pict_stream", response.getEntity().getContent());
		} catch (Exception e) {
			logger.error("requestFileData", e);
			throw e;
		}
		Header [] header=response.getHeaders("Content-Type");
		if(header.length>0){
			MimeType type=allTypes.forName(header[0].getValue());
			data.put("pict_ext", type.getExtension());
		}
		return data;
	}
	
	@Override
	public void openIDLoginRequest(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyOpenIDLogin(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	public Integer getProviderId(){
		return 2;
	}
	public String getProviderName(){
		return "facebook";
	}

	@Override
	public void setUserPrefs(Useracc useracc,String accessToken,String refreshToken) {
		UseraccPrefs up=prefsDao.findById(useracc.getId());
		if(up==null){
			up=new UseraccPrefs();
			up.setUseracc(useracc);
		}
		up.setFacebookAccessToken(accessToken);
		up.setFacebookRefreshToken(refreshToken);
		prefsDao.persist(up);
	}
	@Override
	public Useracc getUserForAccessToken(String accessToken) {
		return prefsDao.findUserByGoogleRequestToke(accessToken);
	}
}
