package org.mfr.data;

// Generated 2011.09.19. 21:08:49 by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.ExifData;
import org.mfr.data.ExifDataHome;

/**
 * Home object for domain model class ExifData.
 * @see org.mfr.data.ExifData
 * @author Hibernate Tools
 */
public class ExifDataHome {

	private static final Log log = LogFactory.getLog(ExifDataHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(ExifData transientInstance) {
		log.debug("persisting ExifData instance");
		try {
			entityManager.persist(transientInstance);
			entityManager.flush();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(ExifData persistentInstance) {
		log.debug("removing ExifData instance");
		try {
			entityManager.remove(persistentInstance);
			entityManager.flush();
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public ExifData merge(ExifData detachedInstance) {
		log.debug("merging ExifData instance");
		try {
			ExifData result = entityManager.merge(detachedInstance);
			entityManager.flush();
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ExifData findById(Integer id) {
		log.debug("getting ExifData instance with id: " + id);
		try {
			ExifData instance = entityManager.find(ExifData.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
