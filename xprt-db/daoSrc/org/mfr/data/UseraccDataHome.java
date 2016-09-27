package org.mfr.data;



import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.UseraccData;
import org.mfr.data.UseraccDataHome;

public class UseraccDataHome {
	private static final Log log = LogFactory.getLog(UseraccDataHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(UseraccData transientInstance) {
		log.debug("persisting UseraccData instance");
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

	public void remove(UseraccData persistentInstance) {
		log.debug("removing UseraccData instance");
		try {
			entityManager.remove(persistentInstance);
			entityManager.flush();
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public UseraccData merge(UseraccData detachedInstance) {
		log.debug("merging UseraccData instance");
		try {
			detachedInstance.setModifyDt(new Date());
			UseraccData result = entityManager.merge(detachedInstance);
			entityManager.flush();
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UseraccData findById(Integer id) {
		log.debug("getting UseraccData instance with id: " + id);
		try {
			UseraccData instance = entityManager.find(UseraccData.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
