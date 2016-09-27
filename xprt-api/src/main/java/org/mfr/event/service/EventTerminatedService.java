package org.mfr.event.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.Useracc;
import org.mfr.data.XEvent;
import org.mfr.rest.security.MfrSecurityContextStore;
import org.mfr.rest.util.MailHelper;
import org.mfr.xprt.data.XEventDao;
import org.mfr.xprt.rest.controller.EventController;
import org.mfr.xprt.rest.controller.XprtDetailController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;

public class EventTerminatedService implements Runnable{
	static Log log = LogFactory.getLog(EventTerminatedService.class);
	private final ScheduledExecutorService terminatedEventsScheduler =
		     Executors.newScheduledThreadPool(1);
	@Autowired
	private XEventDao eventDao;
	@Autowired
	private EventController eventController;
	@Autowired
	private XprtDetailController xprtDetailController;

	@Override
	public synchronized void run() {
		try{
			List<XEvent> eventList=eventDao.findForTerminatedEvents();
			log.debug("set completed for terminated");
			for (XEvent event : eventList) {
				event.setStatus(30);
				eventDao.mergeG(event);
				Locale l=Locale.forLanguageTag("hu");
				
				Authentication authentication=MfrSecurityContextStore.createStaticAuthenticationObject(event.getInitiator());
				Map result=(Map)xprtDetailController.getXprtDetail(authentication, null, null, null, null, null, null, true, "next");
				Set forRank=(Set)result.get("xprtDetail");
				if(forRank!=null && forRank.size()>0){
					if(!event.getInitiator().getId().equals(event.getXprtDetail().getUseracc().getId())){ // we dont send message if the initiator and the xprt are the same
						log.debug("send request ranking based on event["+event.getId()+"] user["+event.getInitiator().getLogin()+"]");
						eventController.sendEventEmail(MailHelper.REQUEST_RANKING,event,event.getInitiator(), l,result);
					}
				}
			}
		}catch(Exception e){
			log.error("error sending ranking request message",e);
		}
		scheduleEvent();
	}
	public void init(){
		scheduleEvent();
	}
	private void scheduleEvent() {
		if(!org.mfr.rest.util.Constants.TESTMODE){
			terminatedEventsScheduler.schedule(this, 15, TimeUnit.MINUTES);
		}
	}
}
