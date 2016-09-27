package org.mfr.event.service;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.XEvent;
import org.mfr.rest.util.MailHelper;
import org.mfr.xprt.data.XEventDao;
import org.mfr.xprt.data.XLangtextDao;
import org.mfr.xprt.rest.controller.EventController;


public class NotificationSenderWorker implements Runnable{
	static Log log = LogFactory.getLog(NotificationSenderWorker.class);
	

	private EventController eventController;
	
	private XEvent event;
	private XEventDao eventDao;
	private XLangtextDao langtextDao;
	private EventNotificationService notificationService;
	
	private static final long marginSecLimit=120000L; // 2 minutes
	
	public NotificationSenderWorker(XEvent event,
			EventNotificationService notificationService,
			XEventDao eventDao,
			XLangtextDao langtextDao,
			EventController eventController){
		this.event=event;
		this.notificationService=notificationService;
		this.eventDao=eventDao;
		this.langtextDao=langtextDao;
		this.eventController=eventController;
	}
	@Override
	public void run() {
		log.debug("notification worker executed for:"+event.getName());		
		try{
			// check time 
			long millis=Math.abs(event.getStartDate().getTime()-System.currentTimeMillis()-event.getNotification());
			if(millis>=marginSecLimit){
				MailHelper.sendLogMail("warning","event notification marginSecLimit exceeded for:"+event.getId()+" notification millis:"+event.getNotification());
				log.warn("event notification marginSecLimit exceeded for:"+event.getId()+" notification minutes:"+millis/1000/60);
			}
			
			// email to host
			Locale l=Locale.forLanguageTag("hu");
			try {
				eventController.sendEventEmail(MailHelper.NOTIFYHOST_EMAIL,event,event.getHost(), l,null);
			} catch (Exception e) {
				log.error("error sending notification",e);
				MailHelper.sendErreReportMail(e, "eventid:"+event.getId());
			}
			if(!event.getInitiator().getId().equals(event.getHost().getId())){
				// email to initiator
				try {
					eventController.sendEventEmail(MailHelper.NOTIFYHOST_EMAIL,event,event.getInitiator(), l,null);
				} catch (Exception e) {
					log.error("error sending notification",e);
					MailHelper.sendErreReportMail(e, "eventid:"+event.getId());
				}
			}
			event.setNotification(0);
			eventDao.merge(event);
		}catch(Exception e){
			log.error("error sending event email",e);
		}finally{
			// schedule nex event sender
			this.notificationService.schedule();
		}
	}
	
	public XEventDao getEventDao() {
		return eventDao;
	}
	public void setEventDao(XEventDao eventDao) {
		this.eventDao = eventDao;
	}

}
