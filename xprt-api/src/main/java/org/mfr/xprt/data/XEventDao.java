package org.mfr.xprt.data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.mfr.data.XEvent;
import org.mfr.data.XEventHome;


public class XEventDao extends XEventHome{
	public static enum STATUS{
		REQUESTED(1),REJECTED(10),ACCEPTED(20),COMPLETED(30),FAIL(40),RESIGNED(50);
		Integer code;
		STATUS(Integer code){
			this.code=code;
		}
		public Integer code(){
			return this.code;
		}
		public static boolean isValid(Integer code){
			STATUS[] stats=values();
			for(int i=0;i<stats.length;i++){
				if(stats[i].code()==code){
					return true;
				}
			}		
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static final String queryForInitiatorOrHost="select * from x_event where (initiator=:initiator or host=:host) ";
	private static final String queryForInitiatorOrHostExcludeInactives="select * from x_event where (initiator=:initiator or host=:host) and status not in (10,50) ";
	private static final String queryForHost="select * from x_event where host=:host and status not in (10,50) ";
	private static final String queryForInitator="select * from x_event where initiator=:initiator and status not in (10,50) ";
	private static final String queryForXprt="select * from x_event where xprtDetail=:xprt and status not in (10,50) ";
	
	private static final String queryForNotifications="select * from x_event where start_date>:from and notification>0 and status in (1,20)  order by start_date";
	
	public List<XEvent>findForInitiatorOrHost(Integer initiator,Integer host){
		Query result = entityManager.createNativeQuery(queryForInitiatorOrHost, XEvent.class)
				.setParameter("initiator", initiator)
				.setParameter("host", initiator);
		return result.getResultList();
	}
	public List<XEvent>findForNextSchedule(int count,Date from){
		Query result = entityManager.createNativeQuery(queryForNotifications, XEvent.class).setParameter("from", from);
		return result.getResultList();
	}
	public List<XEvent>findForTerminatedEvents(){
		Query result = entityManager.createNativeQuery("select * from mfr.x_event where end_date<now() and status in (1,20)", XEvent.class);
		return result.getResultList();
	}	
	public List<XEvent>findForInitiatorOrHost(Integer initiator,Integer host,Date date){
		Query result = entityManager.createNativeQuery(queryForInitiatorOrHost+" and date_trunc('day', start_date)=date_trunc('day', cast(:date as timestamp)) ", XEvent.class)
				.setParameter("initiator", initiator)
				.setParameter("host", initiator)
				.setParameter("date", new Timestamp(date.getTime()));
		return result.getResultList();
	}
	public List<XEvent>findForInitiatorOrHost(Integer initiator,Integer host,Date from,Date to){
		Query result = entityManager.createNativeQuery(queryForInitiatorOrHostExcludeInactives+" and date_trunc('day', start_date)>=:from and date_trunc('day', start_date)<=:to ", XEvent.class)
				.setParameter("initiator", initiator)
				.setParameter("host", host)
				.setParameter("from", from)
				.setParameter("to", to);
		return result.getResultList();
	}
	public List<XEvent>findForHostExcludeInactives(Integer host,Date from,Date to){
		Query result = entityManager.createNativeQuery(queryForHost+" and date_trunc('day', start_date)>=:from and date_trunc('day', start_date)<=:to ", XEvent.class)
				.setParameter("host", host)
				.setParameter("from", from)
				.setParameter("to", to)
				;
		return result.getResultList();
	}
	public List<XEvent>findForXprtExcludeInactives(Integer xprt,Date from,Date to){
		Query result = entityManager.createNativeQuery(queryForXprt+" and ((start_date<=:from and :from<=end_date) or (end_date<=:to and :to<=end_date)", XEvent.class)
				.setParameter("xprt", xprt)
				.setParameter("from", from)
				.setParameter("to", to)
				;
		return result.getResultList();
	}
	public List<XEvent>findForInitiatorExcludeInactives(Integer initiator,Date from,Date to){
		Query result = entityManager.createNativeQuery(queryForInitator+" and ((start_date<=:from and :from<=end_date) or (end_date<=:to and :to<=end_date))", XEvent.class)
				.setParameter("initiator", initiator)
				.setParameter("from", from)
				.setParameter("to", to)
				;
		return result.getResultList();
	}
	public List<XEvent>findNextEventsForUser(Integer user, Date fromDate, int offset,int limit){
		Query result = entityManager.createNativeQuery("select * from x_event where start_date>=:date and (host=:user or initiator=:user) order by start_date asc offset :offset limit :limit ", XEvent.class)
				.setParameter("user", user)
				.setParameter("date", fromDate)
				.setParameter("limit", limit)
		.setParameter("offset", offset);
		return result.getResultList();
	}
	public List<XEvent>findPastEventsForUser(Integer user, Date fromDate, int offset,int limit){
		Query result = entityManager.createNativeQuery("select * from x_event where start_date<:date and (host=:user or initiator=:user) order by start_date desc offset :offset limit :limit ", XEvent.class)
				.setParameter("user", user)
				.setParameter("date", fromDate)
				.setParameter("limit", limit)
		.setParameter("offset", offset);
		return result.getResultList();
	}
}
