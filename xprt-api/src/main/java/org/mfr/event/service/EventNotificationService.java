package org.mfr.event.service;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.XEvent;
import org.mfr.xprt.data.XEventDao;
import org.mfr.xprt.data.XLangtextDao;
import org.mfr.xprt.rest.controller.EventController;
import org.mfr.xprt.rest.controller.LangtextController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class EventNotificationService implements Runnable{
	static Log log = LogFactory.getLog(EventNotificationService.class);
	private final ScheduledExecutorService reloadEventsScheduler =
		     Executors.newScheduledThreadPool(1);
	private final ScheduledExecutorService sendEventsScheduler =
		     Executors.newScheduledThreadPool(1);
	@Autowired
	private XEventDao eventDao;
	@Autowired
	private XLangtextDao langtextDao;
	@Autowired(required=true)
	@Qualifier("eventController")
	private EventController eventController;
	
	private List <EventSchedule> eventScheduleList=new LinkedList<EventSchedule>();
	
	@Override
	public synchronized void run() {
		List<XEvent> eventList=eventDao.findForNextSchedule(1,new Date());
		log.debug("cancel schedules");
		for (EventSchedule schedule : eventScheduleList) {
			log.debug("cancel schedule for event:"+schedule.getEvent().getId());
			schedule.getSchedule().cancel(false);
		}
		eventScheduleList.clear();
		// schedule first
		boolean scheduleNext=true;
		int i=0;
		while(eventList.size()>0 && scheduleNext){
			NotificationSenderWorker worker=new NotificationSenderWorker(eventList.get(i),this,eventDao,langtextDao,eventController);
			XEvent event=eventList.get(0);
			Calendar cal=Calendar.getInstance();
			Calendar eventCal=Calendar.getInstance();
			eventCal.setTime(event.getStartDate());
			log.debug("calendar:"+cal);
			if(cal.before(eventCal) && event.getNotification()>0){
				long delay=event.getStartDate().getTime()-cal.getTimeInMillis()-event.getNotification();
				log.debug("schedule for:"+event.getStartDate()+" delay:"+delay/1000/60 +" minutes");
				ScheduledFuture schedule=sendEventsScheduler.schedule(worker, delay,TimeUnit.MILLISECONDS);
				eventScheduleList.add(new EventSchedule(event, schedule));
				scheduleNext=false;
			}else{
				i++;
			}
		}
		if(scheduleNext){ // there were no event to schedule
			reloadEventsScheduler.schedule(this, 10, TimeUnit.MINUTES);
		}
	}
	public void init(){
		if(!org.mfr.rest.util.Constants.TESTMODE){
			ScheduledFuture schedule=reloadEventsScheduler.schedule(this, 1000, TimeUnit.MILLISECONDS);
			try {
				log.debug("reschedule events:"+schedule.get());
			} catch (Exception e) {
				log.error("execution Exception",e);
			}
		}
	}
	public void schedule(){
		if(eventScheduleList.size()>0){
			init();
		}
	}
}
