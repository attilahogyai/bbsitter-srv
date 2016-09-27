package org.mfr.xprt.rest.controller;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.Useracc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorNotificationController extends AbstractBaseController{
	private AtomicInteger errorCounter=new AtomicInteger();
	private static Logger log = LoggerFactory.getLogger(ErrorNotificationController.class);
	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return null;
	}
	@RequestMapping(value = "/error-notification", method = RequestMethod.POST)
	@ResponseBody
	public Object createEvents(Authentication authentication, HttpServletRequest request) {
		Useracc user=null;
		try{
			user=((Useracc)authentication.getPrincipal());
		}catch(NullPointerException e){}
		int counter=errorCounter.incrementAndGet();
		if(user!=null){
			log.error("CLIENT ERROR("+counter+") email:"+user.getEmail()+" id:"+user.getId());
		}
		log.error("CLIENT ERROR JSON:"+request.getParameter("stack"));
		return null;
	}

	
}
