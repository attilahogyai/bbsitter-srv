package org.mfr.xprt.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.XLangtext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

@RequestMapping("/common")
@Controller
public class LangtextController extends AbstractBaseController<XLangtext>{
	protected final String NAME="langtext";
	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object findObject(HttpServletRequest request){
		return super.findObject(NAME, request);
	}
	@RequestMapping(value="/"+NAME,method = RequestMethod.POST)
	@ResponseBody
	public Object createObject(Authentication authentication,@RequestBody JsonNode json){
		return super.createObject(NAME, json);
	}
	
	@RequestMapping(value="/"+NAME+"/{objId}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateLangtext(@PathVariable Integer objId,@RequestBody JsonNode json,Authentication authentication){
		return super.updateObject(NAME, objId, getIgnorePropertiesForPut(), json);
	}
	@RequestMapping(value="/"+NAME+"/{objId}",method = RequestMethod.DELETE)
	@ResponseBody
	public Object deleteLangtext(@PathVariable Integer objId,Authentication authentication){
		return super.deleteObject(NAME, objId);
	}
	protected Class getClazz(){
		return XLangtext.class;
	}
}
