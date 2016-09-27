package org.mfr.data;

// Generated 2011.09.19. 21:08:49 by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.UseraccPrefs;
import org.mfr.data.UseraccPrefsHome;

/**
 * Home object for domain model class UseraccPrefs.
 * @see org.mfr.data.UseraccPrefs
 * @author Hibernate Tools
 */
public class UseraccPrefsHome extends AbstractDao{

	private static final Log log = LogFactory.getLog(UseraccPrefsHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(UseraccPrefs transientInstance) {
		super.persist(transientInstance);
	}

	public UseraccPrefs merge(UseraccPrefs detachedInstance) {
		return (UseraccPrefs)super.merge(detachedInstance);
	}

	public UseraccPrefs findById(Integer id) {
		log.debug("getting UseraccPrefs instance with id: " + id);
		try {
			UseraccPrefs instance = entityManager.find(UseraccPrefs.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
