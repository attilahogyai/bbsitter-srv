package org.mfr.xprt.rest.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.script.ScriptEngine;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.exception.ConstraintViolationException;
import org.mfr.data.Useracc;
import org.mfr.data.XSession;
import org.mfr.data.XXprtDetail;
import org.mfr.rest.image.ImageConfig;
import org.mfr.rest.image.ImageTools;
import org.mfr.rest.util.HttpHelper;
import org.mfr.rest.util.MailHelper;
import org.mfr.util.ZkUtil;
import org.mfr.xprt.data.UseraccDao;
import org.mfr.xprt.data.UseraccPrefsDao;
import org.mfr.xprt.data.XSessionDao;
import org.mfr.xprt.data.XXprtDetailDao;
import org.mfr.xprt.manager.UserManager;
import org.mfr.xprt.rest.exception.CheckException;
import org.mfr.xprt.rest.exception.NotFound;
import org.mfr.xprt.rest.exception.ServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
public class UserController extends AbstractBaseController<Useracc> {
	
	public static Logger log = LoggerFactory.getLogger(UserController.class);
	public static final String TOKENCACHE = "tokenCache";

	protected final String NAME = "user";

	@Autowired
	UseraccDao userAccDao;
	@Autowired
	XSessionDao xsessionDao;
	
	@Autowired
	XXprtDetailDao xprtDetailDao;
	
	@Autowired
	UserManager userManager;
	
	@Autowired
	UseraccPrefsDao prefsDao;
	
	
	ScriptEngine jsEngine = engineManager.getEngineByName("nashorn");

	@RequestMapping(value = "/token", method = RequestMethod.POST)
	@ResponseBody
	public Object createTokenByLoginName(HttpServletRequest request,
			@RequestParam(value = "username") String opCode,
			@RequestParam(value = "password") String password) {
		log.debug("user[" + opCode + "] password[" + password + "]");

		Useracc useracc = userAccDao.findByLoginAndPassword(opCode, password);
		if (useracc == null || useracc.getStatus()==null || useracc.getStatus().equals(0)) {
			return new ResponseEntity<String>("UNAUTHORIZED", null,
					HttpStatus.UNAUTHORIZED);
		}
		Map<String, Object> result = createUserSession(request, useracc);
		return result;
	}
	@RequestMapping(value = "/googlelogin", method = RequestMethod.POST)
	public void googleLogin(HttpServletRequest request,HttpServletResponse response){
		this.userManager.oAuthLogin("google", request, response);
	}
	@RequestMapping(value = "/oauth2callback", method = RequestMethod.GET)
	public String googleOuth2Callback(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "code") String authToken,@RequestParam(value = "state") String state) {
		log.debug("authToken[" + authToken + "]");
		String forwardTo=parseDomain()+"/#/oauth2callback/"+HttpHelper.urlEncode(authToken)+"/"+HttpHelper.urlEncode(state);
		return "redirect:"+forwardTo; 
	}
	@RequestMapping(value = "/oauth2tokencheck", method = RequestMethod.POST)
	@ResponseBody
	public Object googleOuth2Token(HttpServletRequest request,@RequestParam(value = "code") String authToken) {
		Useracc useracc=userManager.checkGoogleOAuth(authToken);
		if (useracc == null || useracc.getStatus()==null || useracc.getStatus().equals(0)) {
			return new ResponseEntity<String>("UNAUTHORIZED", null,
					HttpStatus.UNAUTHORIZED);
		}
		Map<String, Object> result = createUserSession(request, useracc);
		return result;
	}	
	
	@RequestMapping(value = "/facebooklogin", method = RequestMethod.POST)
	public void facebookLogin(HttpServletRequest request,HttpServletResponse response){
		this.userManager.oAuthLogin("facebook", request, response);
	}	
	@RequestMapping(value = "/foauth2callback", method = RequestMethod.GET)
	public String facebookOuth2Callback(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "code",required=false) String authToken, 
			@RequestParam(value = "error_message",required=false) String errorMessage) {
		if(authToken==null){
			authToken="error";
			log.error("message error:"+errorMessage);
		}
		log.debug("authToken[" + authToken + "]");
		String forwardTo=parseDomain()+"/#/foauth2callback/"+HttpHelper.urlEncode(authToken);
		return "redirect:"+forwardTo; 
	}	
	@RequestMapping(value = "/foauth2tokencheck", method = RequestMethod.POST)
	@ResponseBody
	public Object facebookOuth2Token(HttpServletRequest request,@RequestParam(value = "code") String authToken) {
		Useracc useracc=userManager.checkFacebookOAuth(authToken);
		if (useracc == null || useracc.getStatus()==null || useracc.getStatus().equals(0)) {
			return new ResponseEntity<String>("UNAUTHORIZED", null,
					HttpStatus.UNAUTHORIZED);
		}
		Map<String, Object> result = createUserSession(request, useracc);
		return result;
	}	
	
	
	private Map<String, Object> createUserSession(HttpServletRequest request,
			Useracc useracc) {
		// create Token
		XSession session = new XSession();
		session.setUseracc(useracc);
		if (useracc.getScopes() != null) {
			session.setScopes(useracc.getScopes());
		} else {
			session.setScopes(new String[] { "ROLE_USER" });
		}
		session.setValid(1);
		session.setRemoteAddress(request.getRemoteAddr());
		session.setUserAgent(request.getHeader("user-agent"));
		String token = System.currentTimeMillis() + "-"
				+ request.getSession(true).getId();
		session.setToken(token);
		
		List<XXprtDetail> xprt=xprtDetailDao.findForUser(useracc.getId());
		

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("access_token", token);
		result.put("prevLogin", useracc.getLastLogin());
		xsessionDao.persist(session);
		useracc.setLastLogin(new Date());
		userAccDao.merge(useracc);
		result.put("scope", session.getScopes());
		result.put("username", useracc.getName());
		result.put("userid", useracc.getId());
		result.put("modifyDt", useracc.getModifyDt());
		result.put("profession", useracc.getProfession());
		if(xprt!=null && xprt.size()>0){
			result.put("xprtDetailId", xprt.get(0).getId());
		}
		return result;
	}
	
	@RequestMapping(value = "/useracc/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object getUseraccById(@PathVariable Integer id,Authentication authentication) {
		Useracc useracc=null;
		if(id.equals(0)){
			useracc=checkForAuth(authentication);
		}else{
			useracc=userAccDao.findById(id,Useracc.class);
		}
		setSerializeHidden(false);
		return wrapPayload("useracc", useracc);
	}	
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object getUserById(@PathVariable Integer id,Authentication authentication) {
		Useracc user=checkForAuth(authentication);
		setSerializeHidden(true);
		user.setSensible(false);
		return wrapPayload("user", user);
	}
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object getUser(Authentication authentication) {
		return getUserById(0,authentication);
	}
	
	@RequestMapping(value="/user/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateUser(@PathVariable Integer id,@RequestBody JsonNode inputJson,Authentication authentication){
		ObjectNode userJson=(ObjectNode)inputJson.get("user");
		Useracc user=checkForAuth(authentication);
		Useracc source=this.extractFromJson(userJson, Useracc.class);
		Useracc detail=userAccDao.findById(id,Useracc.class);
		if(id==0){
			detail=userAccDao.findById(user.getId(),Useracc.class);
		}
		
		checkOwner(authentication, detail);
		BeanUtils.copyProperties(source, detail,getIgnorePropertiesForPut("storageLimit", "gender", "privateCode", "useraccDatas", "language", "password", "email", "scopes", "login", "provider", "status", "lastLogin", "newsletter", "useraccPrefs"));
		userAccDao.merge(detail);
		List result=new ArrayList();
		result.add(detail);
		List<XXprtDetail> xprt=xprtDetailDao.findForUser(detail.getId());
		for (XXprtDetail xXprtDetail : xprt) {
			xXprtDetail.setName(detail.getName());
			xXprtDetail.setEmail(detail.getEmail());
			result.add(xXprtDetail);
			xprtDetailDao.mergeG(xXprtDetail);
		}
		
		return wrapPayload(NAME, result);
	}
	@RequestMapping(value="/activate/{code}",method = RequestMethod.GET)
	@ResponseBody
	public Object activateUser(@PathVariable String code){
		Useracc detail=userAccDao.findByPrivateCode(code);
		if(detail==null){
			throw new NotFound("user not found for activation");
		}
		detail.setStatus(1);
		userAccDao.merge(detail);
		return wrapPayload(NAME, detail);
	}
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object signupUser(HttpServletRequest request,
			@RequestParam(required=true,value="l") String language,
			@RequestBody JsonNode userJson) {
		JsonNode userNode=userJson.get("user");
		hasEmpty(userNode);
		
		String name = userNode.get("name").asText();
		String email = userNode.get("email").asText();
		String password = userNode.get("password").asText();
		String target = userNode.get("profession").asText();
		hasEmpty(name, email, password);
		
		
		Useracc ua = new Useracc();
		ua.setEmail(email);
		ua.setLogin(email);
		ua.setName(name);
		ua.setStatus(0);
		ua.setPassword(password);
		userAccDao.encodePassword(ua);
		ua.setProvider(10);
		ua.setPrivateCode(Long.toString(System.currentTimeMillis())
				+ Long.toString(ZkUtil.getRandomLong(100000)));
		try{
			ua.setProfession(Integer.parseInt(target));
		}catch(Exception e){
			ua.setProfession(0);
		}
//		try{
			userManager.newUserInit(ua);
//		}catch(PersistenceException e){
//			if(e.getCause() instanceof ConstraintViolationException){
//				userAccDao.getEntityManager().getTransaction().rollback();
//				return new ResponseEntity<String>("/registration/error_unique_email", HttpStatus.CONFLICT);
//			}
//			throw e;
//		}
		
		try {
			Map arguments = new HashMap();
			arguments.put("addressee", ua);
			Locale l=getLocale(request);
			sendEmail(l, MailHelper.REGISTRATION_EMAIL, ua.getEmail(), arguments);
			sendEmail(l, MailHelper.REGISTRATION_EMAIL, "admin@bbsitter.hu", arguments);
		} catch (Exception e) {
			Useracc user= userAccDao.findById(ua.getId(),Useracc.class);
			if(user!=null){ // remove user in case of email sending error
				userAccDao.remove(user);
			}
			log.error("send mail error:", e);
			throw new RuntimeException (e);
		}
		ua.setSensible(true);
		return wrapPayload("user", ua);
	}
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object forgotPassword(HttpServletRequest request,@RequestParam(required=true,value="email") String email,
			@RequestParam(required=true,value="l") String language) {
		Useracc user=userAccDao.getUserAccForLogin(email);
		if(user==null){
			throw new NotFound("user not found");
		}
		String passwordChangeRequest = ZkUtil.getAlphaNumeric(8);
		log.debug("password: "+passwordChangeRequest);
		user.setPwChangeRequest(passwordChangeRequest);
		userAccDao.merge(user);
		
		Map additionArgs=new HashMap();
		additionArgs.put("addressee", user);
		additionArgs.put("requestid", passwordChangeRequest);
		sendEmail(getLocale(request), MailHelper.FORGOTPASSWORD_EMAIL, user.getEmail(), additionArgs);
		return wrapPayload("status", "ok");
	}
	@RequestMapping(value = "/forgotChange", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object changeForgotPassword(HttpServletRequest request,@RequestParam(required=true,value="email") String email,
			@RequestParam(required=true,value="requestid") String requestid,
			@RequestParam(required=true,value="new_password") String newPassword) {
		Useracc user=userAccDao.getUserAccForLoginAndPwRequest(email,requestid);
		if(user==null){
			throw new NotFound("user not found");
		}
		user.setPassword(newPassword);
		user.setPwChangeRequest(null);
		userAccDao.encodePassword(user);
		userAccDao.merge(user);
		return wrapPayload("status", "ok");
	}
	

	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object changePassword(HttpServletRequest request,@RequestParam(required=true,value="password") String password,
			@RequestParam(required=true,value="new_password") String newPassword,
			Authentication authentication) {
		Useracc user=checkForAuth(authentication);
		Useracc checkUser=userAccDao.findByLoginAndPassword(user.getLogin(), password);
		if(checkUser==null){
			throw new CheckException("wrong old password was given");
		}
		user.setPassword(newPassword);
		userAccDao.encodePassword(user);
		userAccDao.merge(user);
		return wrapPayload("status", "ok");
	}	
	
	
	
	@RequestMapping(value = "/uploadprofileimage", method = RequestMethod.POST)
    public @ResponseBody
    String uploadFileHandler(Authentication authentication,HttpServletRequest request) {
		Useracc user=checkForAuth(authentication);
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent(request);
		ServletContext servletContext = request.getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// Parse the request
		List<FileItem> items=null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {		
			for (FileItem fileItem : items) {
				String ext=HttpHelper.getExt(fileItem.getName());
				InputStream is=fileItem.getInputStream();
				
				userManager.storePicture(user, ext, is);
				
				userAccDao.merge(user);
				
				List<XXprtDetail> xprt=xprtDetailDao.findForUser(user.getId());
				if(xprt!=null && xprt.size()>0){
					xprtDetailDao.merge(xprt.get(0)); // save to signal that the underlaying xprtdetail changed
				}
			}
		} catch (IOException e) {
			log.error("IOError",e);
			throw new ServerError(e.getMessage(),e); 
		}
		return "OK"; 
    }
	@RequestMapping(value = "/profileimage", method = RequestMethod.GET)
    public @ResponseBody
    void getFileHandler(Authentication authentication,
    		@RequestParam(required=false,value="u") String userid,
    		HttpServletResponse response) {
		if(userid==null){
			Useracc user=checkForAuth(authentication);
			userid=Integer.toString(user.getId());
		}
		try {
			response.setContentType("image/jpeg");
			Files.copy(userManager.getProfilePicture(userid), response.getOutputStream());
		} catch (IOException e) {
			log.error("error returning image file",e);
		}
    }
	
	
	private static final String [] exts=new String[]{"jpg","jpeg","png","gif"};
	private boolean removeImage(String path) {
		for (int i = 0; i < exts.length; i++) {
			Path p=Paths.get(path,exts[i]);
			if(p.toFile().delete()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected Class getClazz() {
		return Useracc.class;
	}

}
