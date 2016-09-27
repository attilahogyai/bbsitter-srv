package org.mfr.xprt.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.XLanguage;
import org.mfr.data.XProfession;
import org.mfr.xprt.data.XLanguageDao;
import org.mfr.xprt.data.XProfessionDao;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class LanguageController extends AbstractBaseController<XLanguage> {
	protected final String NAME="language";
	@Autowired
	private XLanguageDao xLanguageDao;
	

	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getAll(HttpServletRequest request){
		return wrapPayload(NAME, xLanguageDao.findAll(XLanguage.class));
	}
	@RequestMapping(value="/"+NAME+"/{langId}",method = RequestMethod.GET)
	@ResponseBody
	public Object get(@PathVariable Integer langId,HttpServletRequest request){
		return wrapPayload(NAME, xLanguageDao.findByIdG(langId,XProfession.class));
	}
	@RequestMapping(value="/"+NAME+"/{langId}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateEvents(@PathVariable Integer langId,@RequestBody JsonNode eventJson,Authentication authentication){
		XLanguage source=this.extractFromJson(eventJson.get(NAME), XLanguage.class);
		XLanguage language=xLanguageDao.findById(langId,XLanguage.class);
		if(language==null){
			return new ResponseEntity<String>("object not found",HttpStatus.NOT_FOUND);
		}
		BeanUtils.copyProperties(source, language,getIgnorePropertiesForPut());
		xLanguageDao.merge(language);
		return wrapPayload(NAME, language);
	}
	
	@RequestMapping(value = "/" + NAME, method = RequestMethod.POST)
	@ResponseBody
	public Object create(Authentication authentication, @RequestBody JsonNode event) {
		XLanguage object=this.extractFromJson(event.get(NAME), XLanguage.class);
		xLanguageDao.persistG(object);
		return wrapPayload(NAME, object);
	}
	
	@Override
	protected Class getClazz() {
		return XProfession.class;
	}


}
