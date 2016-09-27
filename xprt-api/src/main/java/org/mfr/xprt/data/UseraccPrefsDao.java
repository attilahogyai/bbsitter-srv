package org.mfr.xprt.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.Useracc;
import org.mfr.data.UseraccPrefs;
import org.mfr.data.UseraccPrefsHome;

public class UseraccPrefsDao extends UseraccPrefsHome{
	private static final Log log = LogFactory.getLog(UseraccPrefsDao.class);

	public Useracc findUserByGoogleRequestToke(String token){
		log.debug("getting UseraccPrefs for google request token: " + token);
		try {
			UseraccPrefs instance = entityManager.find(UseraccPrefs.class, token);
			log.debug("get successful");
			return instance.getUseracc();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
