package org.mfr.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SiteCssHome extends AbstractDao{

	private static final Log log = LogFactory.getLog(SiteCssHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(SiteCss transientInstance) {
		super.persist(transientInstance);
	}

	public SiteCss merge(SiteCss detachedInstance) {
		return (SiteCss)super.merge(detachedInstance);
	}

	public SiteCss findById(Integer id) {
		log.debug("getting SiteCss instance with id: " + id);
		try {
			SiteCss instance = entityManager.find(SiteCss.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}

