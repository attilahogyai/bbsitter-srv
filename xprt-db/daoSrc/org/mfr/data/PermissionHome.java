package org.mfr.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.Permission;
import org.mfr.data.PermissionHome;

public class PermissionHome extends AbstractDao{
	private static final Log log = LogFactory.getLog(PermissionHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(Permission transientInstance) {
		super.persist(transientInstance);
	}

	public Permission merge(Permission detachedInstance) {
		return (Permission)super.merge(detachedInstance);
	}

	public Permission findById(Integer id) {
		log.debug("getting Permission instance with id: " + id);
		try {
			Permission instance = entityManager.find(Permission.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
