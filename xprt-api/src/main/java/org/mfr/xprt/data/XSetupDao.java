package org.mfr.xprt.data;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.XSetup;
import org.mfr.data.XSetupHome;

public class XSetupDao extends XSetupHome {
	private static final Log log = LogFactory.getLog(XSetupDao.class);
	@SuppressWarnings("unchecked")
	public List<XSetup> findByUser(Integer userId) {
		log.debug("getting XSetup by userId["+userId+"]");
		try {
			Query query=entityManager.createNativeQuery("select * from x_setup where useracc=:userId", XSetup.class).setParameter("userId", userId);
			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}	
	public List<XSetup> findByXprt(Integer xprtId) {
		log.debug("getting XSetup by userId["+xprtId+"]");
		try {
			Query query=entityManager.createNativeQuery("select * from x_setup where xprt_detail=:xprtId", XSetup.class).setParameter("xprtId", xprtId);
			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}	
	
}
