package org.mfr.xprt.rest.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.Useracc;
import org.mfr.data.XEvent;
import org.mfr.data.XLog;
import org.mfr.xprt.data.XEventDao;
import org.mfr.xprt.data.XLogDao;
import org.mfr.xprt.rest.exception.AccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class LogController extends AbstractBaseController<XLog> {
	private static Logger log = LoggerFactory.getLogger(LogController.class);
	protected final String NAME="log";
	@Autowired
	private XLogDao xlogDao;
	@Autowired
	private XEventDao eventDao;
	@RequestMapping(value = "/" + NAME, method = RequestMethod.GET)
	@ResponseBody
	public Object getLog(Authentication authentication, @RequestParam(required=true,value="event") Integer event,HttpServletRequest request) {
		checkForAuth(authentication);		
		XEvent xEvent=eventDao.findById(event, XEvent.class);
		try{
			checkOwner(authentication, xEvent.getInitiator());
		}catch(AccessDenied ae){
			checkOwner(authentication, xEvent.getHost());
		}
		List<XLog> logs=xlogDao.getLogForEvent(event);
		return wrapPayload(NAME, logs);
	}
	
	@RequestMapping(value = "/" + NAME, method = RequestMethod.POST)
	@ResponseBody
	public Object createLog(Authentication authentication, @RequestBody JsonNode event,HttpServletRequest request) {
		Useracc user=checkForAuth(authentication);		
		
		XLog object=this.extractFromJson(event.get(NAME), XLog.class);
		object.setUseracc(user);
		object.setCreateDt(new Timestamp(new Date().getTime()));
		xlogDao.persistG(object);
		Map m=new HashMap();
		mergeObjectToMap(m,object,true);
		return m;
	}
	
	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return XLog.class;
	}
	

}
