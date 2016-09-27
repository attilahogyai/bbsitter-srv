package org.mfr.data;

// Generated 2011.09.19. 21:08:49 by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.PhotoCategory;
import org.mfr.data.PhotoCategoryHome;

/**
 * Home object for domain model class PhotoCategory.
 * @see org.mfr.data.PhotoCategory
 * @author Hibernate Tools
 */
public class PhotoCategoryHome extends AbstractDao{

	private static final Log log = LogFactory.getLog(PhotoCategoryHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(PhotoCategory transientInstance) {
		super.persist(transientInstance);
	}

	public PhotoCategory merge(PhotoCategory detachedInstance) {
		return (PhotoCategory)super.merge(detachedInstance);
	}

	public PhotoCategory findById(Integer id) {
		log.debug("getting PhotoCategory instance with id: " + id);
		try {
			PhotoCategory instance = entityManager.find(PhotoCategory.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
