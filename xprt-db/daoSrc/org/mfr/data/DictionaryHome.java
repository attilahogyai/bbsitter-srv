package org.mfr.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DictionaryHome extends AbstractDao{

	private static final Log log = LogFactory.getLog(DictionaryHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(Dictionary transientInstance) {
		super.persist(transientInstance);
	}

	public Dictionary merge(Dictionary detachedInstance) {
		return (Dictionary)super.merge(detachedInstance);
	}

	public Dictionary findById(Integer id) {
		log.debug("getting Dictionary instance with id: " + id);
		try {
			Dictionary instance = entityManager.find(Dictionary.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
