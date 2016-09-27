package org.mfr.xprt.data;

import java.util.List;

import javax.persistence.Query;

import org.mfr.data.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentDao extends org.mfr.data.CommentDao{
	private static Logger log = LoggerFactory.getLogger(CommentDao.class);
	public List<Comment> findOpinionsForXprt(Integer xprt) {
		log.debug("getting Comments by xprt: " + xprt);
		try {
			
			Query result = entityManager.createNativeQuery("select * from comment where xprt_detail=:xprt and status is not null and status!=0 and source=1 order by create_dt desc", Comment.class)
					.setParameter("xprt", xprt);
			return result.getResultList();			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	public List<Comment> findMessagesForInbox(Integer useracc) {
		log.debug("getting Comments for useracc: " + useracc);
		try {
			Query result = entityManager.createNativeQuery("select * from comment where addressee=:user and status is not null and status!=0 order by create_dt desc", Comment.class)
					.setParameter("user", useracc);
			return result.getResultList();			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	public List<Comment> findMessagesForOutbox(Integer useracc) {
		log.debug("getting Comments for useracc: " + useracc);
		try {
			Query result = entityManager.createNativeQuery("select * from comment where useracc=:user and status is not null and status!=0 and addressee is not null order by create_dt desc", Comment.class)
					.setParameter("user", useracc);
			return result.getResultList();			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
}
