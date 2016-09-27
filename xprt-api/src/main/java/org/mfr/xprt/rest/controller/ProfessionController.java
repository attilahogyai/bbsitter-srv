package org.mfr.xprt.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.XProfession;
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
public class ProfessionController extends AbstractBaseController<XProfession> {
	protected final String NAME="profession";
	@Autowired
	private XProfessionDao xProfessionDao;
	

	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getAll(HttpServletRequest request){
		return wrapPayload(NAME, xProfessionDao.findAll());
	}
	@RequestMapping(value="/"+NAME+"/{professionId}",method = RequestMethod.GET)
	@ResponseBody
	public Object get(@PathVariable Integer professionId,HttpServletRequest request){
		return wrapPayload(NAME, xProfessionDao.findByIdG(professionId,XProfession.class));
	}
	@RequestMapping(value="/"+NAME+"/{professionId}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateEvents(@PathVariable Integer professionId,@RequestBody JsonNode eventJson,Authentication authentication){
		XProfession source=this.extractFromJson(eventJson.get(NAME), XProfession.class);
		XProfession profession=xProfessionDao.findById(professionId,XProfession.class);
		if(profession==null){
			return new ResponseEntity<String>("object not found",HttpStatus.NOT_FOUND);
		}
		BeanUtils.copyProperties(source, profession,getIgnorePropertiesForPut());
		xProfessionDao.merge(profession);
		return wrapPayload(NAME, profession);
	}
	
	@RequestMapping(value = "/" + NAME, method = RequestMethod.POST)
	@ResponseBody
	public Object create(Authentication authentication, @RequestBody JsonNode event) {
		XProfession object=this.extractFromJson(event.get(NAME), XProfession.class);
		xProfessionDao.persistG(object);
		return wrapPayload(NAME, object);
	}
	
	@Override
	protected Class getClazz() {
		return XProfession.class;
	}

}
