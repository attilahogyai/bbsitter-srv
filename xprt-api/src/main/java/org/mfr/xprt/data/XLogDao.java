package org.mfr.xprt.data;

import java.util.List;

import org.mfr.data.XLog;
import org.mfr.data.XLogHome;

public class XLogDao extends XLogHome{
	public List<XLog> getLogForEvent(Integer event){
		 List<XLog> logs = (List<XLog>) entityManager
			.createQuery(
					"from XLog where event=:event order by id desc")
			.setParameter("event", event)
			.getResultList();
		return logs;
}
}
