package org.mfr.xprt.data;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.mfr.data.Rank;
import org.mfr.data.RankHome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RankDao extends RankHome {
	private static Logger log = LoggerFactory.getLogger(RankDao.class);
	public List<Rank> findAvgByXprt(Integer xprt,Integer type) {
		log.debug("get Rank for xprt: " + xprt+" type:"+type);
		try {
			Query result = entityManager.createNativeQuery("select * from rank where xprt_detail=:xprt and rank_type=:type order by id", Rank.class)
					.setParameter("xprt", xprt)
					.setParameter("type", type);
			return result.getResultList();			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	public List<Rank> findAvgByXprt(Integer xprt) {
		log.debug("get Rank for xprt: " + xprt);
		try {
			Query result = entityManager.createNativeQuery("select * from rank where xprt_detail=:xprt and rank_type>=10 and rank_type<100 order by id", Rank.class)
					.setParameter("xprt", xprt);
			return result.getResultList();			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	public List<Rank> findByXprtAndUser(Integer xprt,Integer user) {
		log.debug("get Rank for xprt: " + xprt);
		try {
			Query result = entityManager.createNativeQuery("select * from rank where xprt_detail=:xprt and useracc=:user and rank_type<10 and rank_type>=1 order by id", Rank.class)
					.setParameter("xprt", xprt)
					.setParameter("user", user);
			return result.getResultList();			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	public Object calcAvgXprt(Integer xprt,int type) {
		log.debug("calculating Rank for xprt: " + xprt+" type:"+type);
		try {
			Query result = entityManager.createNativeQuery("select xprt_detail,rank_type,avg(rank) from mfr.rank where xprt_detail=:xprt and rank_type=:type group by xprt_detail,rank_type ")
					.setParameter("xprt", xprt)
					.setParameter("type", type);
			
			return result.getSingleResult();
		} catch (NoResultException re) {
			return null;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Object calcTotalAvg(Integer xprt) {
		log.debug("calculating Rank for xprt: " + xprt);
		try {
			Query result = entityManager.createNativeQuery("select xprt_detail,avg(rank) from mfr.rank where xprt_detail=:xprt and rank_type>=10 and rank_type<100 group by xprt_detail")
					.setParameter("xprt", xprt);
			return result.getSingleResult();
		} catch (NoResultException re) {
			return null;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}
