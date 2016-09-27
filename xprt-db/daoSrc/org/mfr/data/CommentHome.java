package org.mfr.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommentHome extends AbstractDao{

	private static final Log log = LogFactory.getLog(CommentHome.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(Comment transientInstance) {
		super.persist(transientInstance);
	}

	public Comment merge(Comment detachedInstance) {
		return (Comment)super.merge(detachedInstance);
	}

	public Comment findById(Integer id) {
		log.debug("getting Category instance with id: " + id);
		try {
			Comment instance = entityManager.find(Comment.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
