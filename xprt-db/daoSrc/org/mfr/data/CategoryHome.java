package org.mfr.data;

// Generated 2011.09.19. 21:08:49 by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.Category;
import org.mfr.data.CategoryHome;

/**
 * Home object for domain model class Category.
 * @see org.mfr.data.Category
 * @author Hibernate Tools
 */
public class CategoryHome extends AbstractDao{

	private static final Log log = LogFactory.getLog(CategoryHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(Category transientInstance) {
		super.persist(transientInstance);
	}

	public Category merge(Category detachedInstance) {
		return (Category)super.merge(detachedInstance);
	}

	public Category findById(Integer id) {
		log.debug("getting Category instance with id: " + id);
		try {
			Category instance = entityManager.find(Category.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
