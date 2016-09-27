package org.mfr.data;

// Generated 2011.09.19. 21:08:49 by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.Photo;
import org.mfr.data.PhotoHome;

/**
 * Home object for domain model class Photo.
 * @see org.mfr.data.Photo
 * @author Hibernate Tools
 */
public class PhotoHome {

	private static final Log log = LogFactory.getLog(PhotoHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(Photo transientInstance) {
		log.debug("persisting Photo instance");
		try {
			entityManager.persist(transientInstance);
			entityManager.flush();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Photo persistentInstance) {
		log.debug("removing Photo instance");
		try {
			entityManager.remove(persistentInstance);
			entityManager.flush();
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Photo merge(Photo detachedInstance) {
		log.debug("merging Photo instance");
		try {
			Photo result = entityManager.merge(detachedInstance);
			entityManager.flush();
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Photo findById(Integer id) {
		log.debug("getting Photo instance with id: " + id);
		try {
			Photo instance = entityManager.find(Photo.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
