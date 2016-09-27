package org.mfr.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StyleConfigHome  extends AbstractDao{
	private static final Log log = LogFactory.getLog(StyleConfigHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(StyleConfig transientInstance) {
		super.persist(transientInstance);
	}

	public StyleConfig merge(StyleConfig detachedInstance) {
		return (StyleConfig)super.merge(detachedInstance);
	}

	public StyleConfig findById(Integer id) {
		log.debug("getting StyleConfig instance with id: " + id);
		try {
			StyleConfig instance = entityManager.find(StyleConfig.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
