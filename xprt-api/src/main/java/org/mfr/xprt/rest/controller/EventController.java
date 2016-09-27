package org.mfr.xprt.rest.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.minidev.json.JSONObject;

import org.joda.time.DateTime;
import org.mfr.data.Address;
import org.mfr.data.Useracc;
import org.mfr.data.XEvent;
import org.mfr.data.XLog;
import org.mfr.event.service.EventNotificationService;
import org.mfr.rest.util.MailHelper;
import org.mfr.xprt.data.XEventDao;
import org.mfr.xprt.data.XLogDao;
import org.mfr.xprt.rest.exception.AccessDenied;
import org.mfr.xprt.rest.exception.AuthException;
import org.mfr.xprt.rest.exception.CheckException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@Component
@Qualifier("eventController")
public class EventController extends AbstractBaseController<XEvent>{
	
	
	private static Logger log = LoggerFactory.getLogger(EventController.class);
	protected final String NAME="event";
	@Autowired
	private XEventDao eventDao;
	@Autowired
	private XLogDao logDao;	
	
	@Autowired
	private EventNotificationService eventNotificationService;
	
	private static final Integer DEFAULT_NORIFICATION=1800000;
	
	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getEvents(Authentication authentication,HttpServletRequest request){
		if(request.getParameter("id")!=null){
			Useracc user=checkForAuth(authentication);
			XEvent e=eventDao.findById(Integer.parseInt(request.getParameter("id")),XEvent.class);
			try{
				checkOwner(authentication, e.getInitiator());
			}catch(AccessDenied ae){
				checkOwner(authentication, e.getHost());
			}
			setAccessRight(authentication,e);
			return wrapPayload(NAME, e);
		}else if(request.getParameter("date")!=null){
			Useracc user=checkForAuth(authentication);
			Date date=null;
			try {
				date=getDateFormatter().parse(request.getParameter("date"));
				List<XEvent> eventList=eventDao.findForInitiatorOrHost(user.getId(),user.getId(),date);
				setAccessRight(authentication,eventList);
				return wrapPayload(NAME, eventList);
			} catch (ParseException e) {log.error("dateparseerror",e);}
			return null;
		}else if(request.getParameter("week")!=null){
			Integer userid=0;
			if(request.getParameter("host")!=null){
				userid=Integer.parseInt(request.getParameter("host"));
			}else{
				Useracc user=checkForAuth(authentication);
				userid=user.getId();
			}
			Date date=null;
			try {
				date=getDateFormatter().parse(request.getParameter("week"));
				DateTime dt=new DateTime(date);
				int offset=dt.getDayOfWeek()-1;
				DateTime startOfWeek=dt.minusDays(offset);
				DateTime endOfWeek=startOfWeek.plusDays(6);
				List<XEvent> eventList=eventDao.findForInitiatorOrHost(userid,userid,startOfWeek.toDate(),endOfWeek.toDate());
				setAccessRight(authentication,eventList);
				return wrapPayload(NAME, eventList);
			} catch (ParseException e) {log.error("dateparseerror",e);}
			return null;
		}else if(request.getParameter("fromDate")!=null && request.getParameter("host")!=null){
			Date fromDate=null;
			Useracc user=null;
			if(authentication!=null){
				user=((Useracc)authentication.getPrincipal());
			}
			try {
				fromDate=getDateFormatter().parse(request.getParameter("fromDate"));
				DateTime dt=new DateTime(fromDate);
				DateTime toDate=dt.plusDays(6);
				List<XEvent> eventList=eventDao.findForInitiatorOrHost(Integer.parseInt(request.getParameter("host")),Integer.parseInt(request.getParameter("host")),dt.toDate(),toDate.toDate());
//				List<XEvent> eventList=eventDao.findForHost(Integer.parseInt(request.getParameter("host")),
//						fromDate,toDate.toDate());
				setAccessRight(authentication,eventList);
				Map result=initResultMap("event");
				mergeObjectToMap(result,eventList,false,getIgnorePropertiesForSerializer("host","xprtDetail","address"));
				return result;
			}catch(ParseException pe){
				log.error("ParseException",pe);
			}
		}else if(request.getParameter("direction")!=null ){
			
			Useracc user=checkForAuth(authentication);
			Date today=new Date();
			List<XEvent> eventList=null;
			if(request.getParameter("direction").equals("next")){
				eventList=eventDao.findNextEventsForUser(user.getId(), today, 0, 100);
			}else{
				eventList=eventDao.findPastEventsForUser(user.getId(), today, 0, 100);
			}
			Map result=initResultMap("event");
			mergeObjectToMap(result,eventList,false,null);
			return result;
		}
		Useracc user=checkForAuth(authentication);
		return wrapPayload(NAME, eventDao.findForInitiatorOrHost(user.getId(),user.getId()));
	}
	private void setAccessRight(Authentication authentication,List<XEvent> eventList){
		for (XEvent xEvent : eventList) {
			setAccessRight(authentication, xEvent);
		}
	}
	private void setAccessRight(Authentication authentication, XEvent xEvent) {
		if(!hasAccessRight(authentication, xEvent)){
			xEvent.setSensible(true);
		}
	}
	private boolean hasAccessRight(Authentication authentication,XEvent event){
		try{
			checkOwner(authentication, event.getInitiator());
			event.getInitiator().setSensible(false);
			event.getHost().setSensible(false);
		}catch(AuthException ae){
			try{
				checkOwner(authentication, event.getHost());
			}catch(AuthException ad){
				return false;
			}
		}
		return true;
	}
	@RequestMapping(value="/"+NAME+"/{eventId}",method = RequestMethod.PUT)
	@ResponseBody
	@Transactional
	public Object updateEvents(@PathVariable Integer eventId,@RequestBody JsonNode eventJson,
			@RequestParam (value="l",required=true) String l,
			Authentication authentication,HttpServletRequest request){ //org.mfr.xprt.rest.controller.EventController@2d250788, org.mfr.xprt.rest.controller.EventController@15dacc68
		log.debug("updateEvents called on "+this);
		Useracc user=checkForAuth(authentication);
		JsonNode eventNode=(((ObjectNode)eventJson).get("event"));
		String actionComment=null;
		if(eventNode!=null && eventNode.get("actionComment")!=null){
			String c=((ObjectNode)eventNode).get("actionComment").asText().trim();
			if(!c.equals("null") && !c.equals("")){
				actionComment=c;
			}
			((ObjectNode)eventNode).remove("actionComment");
		}
		
		XEvent source=this.extractFromJson(eventJson.get(NAME), XEvent.class);
		
		XEvent event=eventDao.findById(eventId,XEvent.class);
		
		
		// check status
		Integer newStatus=source.getStatus();
		Integer oldStatus=event.getStatus();
		
		boolean isInitiator=source.getInitiator()!=null && user.getId().equals(source.getInitiator().getId()); 
		boolean isHost=source.getHost()!=null && user.getId().equals(source.getHost().getId());
		
		// check date
		Date newStartDate=source.getStartDate();
		Date oldStartDate=event.getStartDate();
		Date newEndDate=source.getEndDate();
		Date oldEndDate=event.getEndDate();
		boolean startDateChange=!newStartDate.equals(oldStartDate);
		boolean endDateChange=!newEndDate.equals(oldEndDate);		
		
		
		List<XEvent> eventList=eventDao.findForInitiatorExcludeInactives(event.getInitiator().getId(), newStartDate, newEndDate);
		if(eventList!=null && eventList.size()>0 && !eventList.get(0).getId().equals(event.getId())){
			return new ResponseEntity<String>("/event/time_period_reserved",HttpStatus.CONFLICT);
		}

		
		if(event!=null && !isInitiator && !isHost){
			return new ResponseEntity<String>(YOU_ARE_NOT_OWNER_OF_OBJECT,HttpStatus.FORBIDDEN);
		}
		if(event.getStartDate().before(new Date())){ // past items are not allowed to modify, status update is possible
			copyBeans(source, event,getIgnorePropertiesForPut("initiator","host","xprtDetail","name","startDate","endDate","location","desc"));
		}else{
			copyBeans(source, event,getIgnorePropertiesForPut("initiator","host","xprtDetail"));
		}
		log.debug("oldStatus["+oldStatus+"] newStatus["+newStatus+"]");
		event.setStatus(source.getStatus());
		if(!XEventDao.STATUS.isValid(event.getStatus())){
			throw new CheckException(event.getStatus()+" is invalid status");
		}
		event.setModifyDt(new Timestamp(new Date().getTime()));
		
		
		eventDao.evict(event.getInitiator());
		log.debug("update event:"+event.toString());
		eventDao.merge(event);
		// email sending if the initiator does not match with host
		
		if(!isInitiator || !isHost){
			Map additionArgs=new HashMap();

			additionArgs.put("statusChange", !oldStatus.equals(newStatus));
			additionArgs.put("startDateChange", startDateChange);
			additionArgs.put("endDateChange", endDateChange);
			if(actionComment!=null){
				additionArgs.put("actionComment", actionComment);
			}
			additionArgs.put("statusText", getText("event_status", event.getStatus().toString(), l));
			if(isInitiator){
				additionArgs.put("modifier", event.getInitiator());
				sendEventEmail(MailHelper.UPDATEAPPOINTMENT_EMAIL,event,event.getHost(), getLocale(request),additionArgs);
			}else{
				additionArgs.put("modifier", event.getHost());
				sendEventEmail(MailHelper.UPDATEAPPOINTMENT_EMAIL,event,event.getInitiator(), getLocale(request),additionArgs);
			}
		}
		
		// create log
		XLog log=new XLog();
		log.setUseracc(user);
		log.setCreateDt(new Timestamp(new Date().getTime()));
		log.setComment(actionComment);
		log.setEvent(event.getId());
		log.setLog("event_updated");
		log.setObjectData(event.toString());
		logDao.persist(log);
		
		eventNotificationService.schedule();
		return wrapPayload(NAME, event);
	}
	@RequestMapping(value="/"+NAME+"/{eventId}",method = RequestMethod.DELETE)
	@ResponseBody
	public Object deleteEvents(@PathVariable Integer eventId,Authentication authentication){
		XEvent event=eventDao.findById(eventId,XEvent.class);
		Useracc user=checkForAuth(authentication);
		if(event!=null && (!event.getHost().getId().equals(user.getId()) && !event.getInitiator().getId().equals(user.getId())) ){
			return new ResponseEntity<String>(YOU_ARE_NOT_OWNER_OF_OBJECT,HttpStatus.FORBIDDEN);
		}
		eventDao.remove(event);
		
		// create log
		XLog log=new XLog();
		log.setUseracc(user);
		log.setCreateDt(new Timestamp(new Date().getTime()));
		log.setEvent(event.getId());
		log.setLog("event deleted :"+event.toString());
		logDao.persist(log);		
		
		return wrapPayload(NAME, event);
	}
	@RequestMapping(value = "/" + NAME, method = RequestMethod.POST)
	@ResponseBody
	public Object createEvents(Authentication authentication, @RequestBody JsonNode event,HttpServletRequest request) {
		Useracc user=checkForAuth(authentication);
		
		JsonNode eventNode=(((ObjectNode)event).get("event"));
		if(eventNode!=null && eventNode.get("actionComment")!=null){
			((ObjectNode)eventNode).remove("actionComment");
		}		

		XEvent object=this.extractFromJson(event.get(NAME), XEvent.class);
		object.setInitiator(user);
		// fill serilized objects 
		if(object.getHost()==null || object.getHost().getId().equals(0)){
			object.setHost(user);
		}
		if(object.getXprtDetail().getId().equals(0)){
			object.setXprtDetail(null);
		}
		
		if(!object.getHost().getId().equals(object.getInitiator().getId())){
			Useracc host=(Useracc)eventDao.findByIdG(object.getHost().getId(), Useracc.class);
			object.setHost(host);
			object.setStatus(XEventDao.STATUS.REQUESTED.code());
		}else{
			object.setStatus(XEventDao.STATUS.ACCEPTED.code());
		}
		
		List<XEvent> eventList=eventDao.findForInitiatorExcludeInactives(object.getInitiator().getId(), object.getStartDate(), object.getEndDate());
		if(eventList!=null && eventList.size()>0){
			return new ResponseEntity<String>("/event/time_period_reserved",HttpStatus.CONFLICT);
		}
		
		object.setNotification(DEFAULT_NORIFICATION);
		object.setCreateDt(new Timestamp(new Date().getTime()));
		eventDao.persistG(object);
		if(object.getStatus().equals(XEventDao.STATUS.REQUESTED.code())){
			sendEventEmail(MailHelper.REQUESTAPPOINTMENT_EMAIL,object,object.getHost(), getLocale(request),null);
		}
		
		
		// create log
		XLog log=new XLog();
		log.setUseracc(user);
		log.setCreateDt(new Timestamp(new Date().getTime()));
		log.setEvent(object.getId());
		log.setObjectData(object.toString());		
		log.setLog("event_created");
		logDao.persist(log);	
		
		eventNotificationService.schedule();
		
		Map m=new HashMap();
		mergeObjectToMap(m,object,true);
		return m;
	}

	public void sendEventEmail(String template, XEvent event,Useracc addressee, Locale locale, Map additionArgs){
		String language="hu";
		Map arguments = buildMailArguments(event,language);
		
		String domain=parseDomain();
		arguments.put("domain", domain);
		arguments.put("l", locale.getLanguage().toLowerCase());
		arguments.put("addressee", addressee);
		if(additionArgs!=null){
			arguments.putAll(additionArgs);
		}
		if(event.getAddress()!=null){
			event.setAddress((Address)this.eventDao.findByIdG(event.getAddress().getId(), Address.class));
		}
		
		try {
			sendEmailAsync(locale, template, addressee.getEmail(), arguments);
		} catch (Exception e) {
			log.error("send mail error:", e);
			throw (RuntimeException)e;
		}
		
	}

	public Map buildMailArguments(XEvent event,String lang) {
		Map arguments = new HashMap();
		arguments.put("initiator", event.getInitiator());
		arguments.put("statusText", getText("event_status", event.getStatus().toString(), lang));
		arguments.put("host", event.getHost());
		arguments.put("event", event);
		arguments.put("df", getDateFormatter());
		return arguments;
	}
	
	@Override
	protected Class getClazz() {
		return XEvent.class;
	}
}

