package org.mfr.xprt.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mfr.data.Useracc;
import org.mfr.rest.image.ImageConfig;
import org.mfr.rest.image.ImageTools;
import org.mfr.xprt.data.UseraccDao;
import org.mfr.xprt.data.UseraccPrefsDao;
import org.mfr.xprt.manager.oauth.IOAuthService;
import org.mfr.xprt.rest.controller.UserController;
import org.mfr.xprt.rest.exception.NotFound;
import org.mfr.xprt.rest.exception.ServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserManager {
private static Logger log = LoggerFactory.getLogger(UserManager.class);
	
	@Autowired
	private Map<String,IOAuthService> oAuthServiceMap=null;
	
	@Autowired
	private UseraccDao useraccDao;
	@Autowired
	private UseraccPrefsDao userPrefsDao;
	
	
	public void oAuthLogin(String provider,HttpServletRequest request,HttpServletResponse response) {
		if(oAuthServiceMap.get(provider+"OAuthService")!=null){
			oAuthServiceMap.get(provider+"OAuthService").oAuthLoginRequest(request, response);
		}else{
			log.error("provider ["+provider+"] not found");
		}
	}	

	public Map<String, IOAuthService> getoAuthServiceMap() {
		return oAuthServiceMap;
	}

	public void setoAuthServiceMap(Map<String, IOAuthService> oAuthServiceMap) {
		this.oAuthServiceMap = oAuthServiceMap;
	}	
	public Useracc checkFacebookOAuth(String authCode){
		return checkOAuth("facebookOAuthService",authCode);
	}
	public Useracc checkGoogleOAuth(String authCode){
		return checkOAuth("googleOAuthService",authCode);
	}
	private static boolean deleteFile(Path p) {
		if (!p.toFile().delete()) {
			log.error("unable to delete file:" + p, new IOException(
					"unable to delete "));
			return false;
		}
		return true;
	}

	public Path getProfilePicture(String id) {
		Path p1=Paths.get(ImageConfig.PROFILECONFIG.getPath(id+"-profile.png"));
		Path p2=Paths.get(ImageConfig.PROFILECONFIG.getPath(id+"-profile.jpg"));
		Path p3=Paths.get(ImageConfig.PROFILECONFIG.getPath(id+"-profile.jpeg"));
		if(p1.toFile().exists()){
			return p1;
		}else if(p2.toFile().exists()){
			return p2;
		}else if(p3.toFile().exists()){
			return p3;
		}else{
			return Paths.get(ImageConfig.PROFILECONFIG.getPath("profile.png"));
		}
	}
	public void storePicture(Useracc user, String ext, InputStream is)
			throws IOException {
		Path tmpTarget=Paths.get(ImageConfig.TMPCONFIG.getPath(user.getId()+"-profile"+ext.toLowerCase()));
		log.debug("moveTo picture to tmp:"+tmpTarget);
		Files.copy(is, tmpTarget, StandardCopyOption.REPLACE_EXISTING);
		try {
			try{
				Path p=getProfilePicture(Integer.toString(user.getId()));
				if(!p.endsWith("profile.png")){
					deleteFile(p);
				}
			}catch(NotFound f){}
			ImageTools.resizeImages(ImageConfig.PROFILECONFIG, new File(ImageConfig.PROFILECONFIG.getPath(".")), true, tmpTarget.toString());
			deleteFile(tmpTarget);					
			user.setImage(true);
			if(user.getImagec()==null){
				user.setImagec(1);
			}else{
				user.setImagec(user.getImagec()+1);
			}
		} catch (Exception e) {
			log.error("error processing image",e);
			throw new ServerError(e.getMessage(),e); 
		}
	}	
	private Useracc checkOAuth(String provider,String authCode){
		IOAuthService service=this.oAuthServiceMap.get(provider);
        if(authCode!=null){
        	String accessToken=null;
        	String refreshToken=null;
        	try{
        		String []tokens=service.requestForRefreshAndAccessToken(authCode);
        		refreshToken=tokens[0];
        		accessToken=tokens[1];
        	}catch(Exception e){
        		log.debug("get access Token error try refresh token",e);
        		// refresh
        	}
        	try {
        		if(accessToken!=null){
        			Map userData=service.requestUserData(accessToken);
        			String email=(String)userData.get("email");
        			String gender=(String)userData.get("gender");
        			String name=(String)userData.get("name");
					Useracc user=useraccDao.getUserAccForLogin(email);
					if(user!=null){
						user.setPrivateCode(authCode);
						log.debug("external user exists in DB");
					}else{
						log.debug("user not found in database create record for user");
						user=new Useracc();
						user.setProvider(service.getProviderId());
						user.setEmail(email);
						user.setName(name);
						user.setLogin(email);
						user.setPrivateCode(authCode);
						user.setStatus(1);
						// save session // make login
						if(gender!=null){
							user.setGender(gender.equals("mail")?1:2);
						}
						newUserInit(user);
					}
					if(userData.get("pict_stream")!=null){
						storePicture(user, (String)userData.get("pict_ext"), (InputStream)userData.get("pict_stream"));
					}
					service.setUserPrefs(user, accessToken, refreshToken);
					return user;
        		}
			} catch (Exception e) {
				log.error("checkAuth",e);
			}
        }
       	return null;
	}	
	public void newUserInit(Useracc user) {
		user.setScopes(new String[]{"ROLE_USER"});
		useraccDao.persist(user);
	}
}
