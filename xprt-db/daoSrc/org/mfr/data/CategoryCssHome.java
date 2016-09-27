package org.mfr.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CategoryCssHome extends AbstractDao{

	private static final Log log = LogFactory.getLog(CategoryCssHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(CategoryCss transientInstance) {
		super.persist(transientInstance);
	}

	public CategoryCss merge(CategoryCss detachedInstance) {
		return (CategoryCss)super.merge(detachedInstance);
	}

	public CategoryCss findById(Integer id) {
		log.debug("getting CategoryCss instance with id: " + id);
		try {
			CategoryCss instance = entityManager.find(CategoryCss.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}

