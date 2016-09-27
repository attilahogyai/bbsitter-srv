package org.mfr.xprt.data;

import javax.persistence.NoResultException;

import org.mfr.data.XSession;
import org.mfr.data.XSessionHome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XSessionDao extends XSessionHome {
	private static Logger log = LoggerFactory.getLogger(XSessionDao.class);
	public XSession getSessionByAuthToken(String token) {
		try {
			log.debug("get session for token["+token+"]");
			XSession session = (XSession) entityManager
					.createQuery(
							"from XSession where token=:token and valid=1")
					.setParameter("token", token).getSingleResult();
			return session;
		} catch (NoResultException e) {
			log.debug("no result");
			return null;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
