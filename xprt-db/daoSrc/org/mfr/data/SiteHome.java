package org.mfr.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SiteHome extends AbstractDao{

	private static final Log log = LogFactory.getLog(SiteHome.class);

	public void persist(Site transientInstance) {
		super.persist(transientInstance);
	}

	public Site merge(Site detachedInstance) {
		return (Site)super.merge(detachedInstance);
	}
	public Site findById(Integer id) {
		log.debug("getting Site instance with id: " + id);
		try {
			Site instance = entityManager.find(Site.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
