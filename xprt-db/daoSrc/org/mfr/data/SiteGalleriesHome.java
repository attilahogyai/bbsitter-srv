package org.mfr.data;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SiteGalleriesHome {
	private static final Log log = LogFactory.getLog(SiteGalleriesHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(SiteGalleries transientInstance) {
		log.debug("persisting SiteGalleries instance");
		try {
			transientInstance.setModifyDt(new Date());
			entityManager.persist(transientInstance);
			entityManager.flush();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(SiteGalleries persistentInstance) {
		log.debug("removing SiteGalleries instance");
		try {
			entityManager.remove(persistentInstance);
			entityManager.flush();
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public SiteGalleries merge(SiteGalleries detachedInstance) {
		log.debug("merging SiteGalleries instance");
		try {
			detachedInstance.setModifyDt(new Date());
			SiteGalleries result = entityManager.merge(detachedInstance);
			entityManager.flush();
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SiteGalleries findById(Integer id) {
		log.debug("getting SiteGalleries instance with id: " + id);
		try {
			SiteGalleries instance = entityManager.find(SiteGalleries.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
