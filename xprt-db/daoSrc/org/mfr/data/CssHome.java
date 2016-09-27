package org.mfr.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CssHome extends AbstractDao{

	private static final Log log = LogFactory.getLog(CssHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(Css transientInstance) {
		super.persist(transientInstance);
	}

	public Css merge(Css detachedInstance) {
		return (Css)super.merge(detachedInstance);
	}

	public Css findById(Integer id) {
		log.debug("getting Css instance with id: " + id);
		try {
			Css instance = entityManager.find(Css.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
