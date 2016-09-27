package org.mfr.xprt.rest.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.Useracc;
import org.mfr.data.XSession;
import org.mfr.data.XSetup;
import org.mfr.xprt.data.XSearchManagerDao;
import org.mfr.xprt.data.XSetupDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class SetupController extends AbstractBaseController<XSetup>{
	@Autowired
	private XSetupDao xSetupDao;
	@Autowired
	private XSearchManagerDao xSearchManagerDao;
	
	protected final String NAME="setup";
	
	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object findMySetup(Authentication authentication,HttpServletRequest request,@RequestParam(required=false,value="useracc") Integer useracc, @RequestParam(required=false,value="xprtid") Integer expert){
		List<XSetup> setupList = null;
		
		if(request.getParameter("useracc")!=null){
			setupList=xSetupDao.findByUser(useracc);
		}else if(expert!=null){
			setupList=xSetupDao.findByXprt(expert);
		}else{
			Useracc user=checkForAuth(authentication);
			setupList=xSetupDao.findByUser(user.getId());
		}
		return wrapPayload(NAME, setupList);
	}
	
	@RequestMapping(value="/setup/{objId}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateSetup(@PathVariable Integer objId,@RequestBody JsonNode json,Authentication authentication){
		XSetup target=(XSetup)xSetupDao.findById(objId,XSetup.class);
		checkOwner(authentication, target.getUseracc());
		Object source=this.extractFromJson(json.get(NAME), getClazz());
		BeanUtils.copyProperties(source, target,getIgnorePropertiesForPut("useracc"));
		commonDao.mergeG(target);
		Map result=initResultMap(NAME);
		mergeObjectToMap(result,target,false,getIgnorePropertiesForSerializer("host","xprtDetail","useracc"));
		return result;
	}
	@RequestMapping(value="/"+NAME,method = RequestMethod.POST)
	@ResponseBody
	public Object createSetup(Authentication authentication,@RequestBody JsonNode event){
		XSetup object=this.extractFromJson(event.get(NAME), XSetup.class);
		object.setUseracc((Useracc)authentication.getPrincipal());
		generalDao.persistG(object);
		Map result=initResultMap(NAME);
		mergeObjectToMap(result,object,false,getIgnorePropertiesForSerializer("host","xprtDetail","useracc"));
		return result;
	}
	
	@Override
	protected Class getClazz() {
		return XSetup.class;
	}	
}
