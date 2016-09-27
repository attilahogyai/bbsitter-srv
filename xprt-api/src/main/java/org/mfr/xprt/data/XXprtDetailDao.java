package org.mfr.xprt.data;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.mfr.data.Rank;
import org.mfr.data.XXprtDetail;
import org.mfr.data.XXprtDetailHome;

public class XXprtDetailDao extends XXprtDetailHome {
	private static final String queryForDetail="select * from x_xprt_detail where useracc=:user";
	private static final String queryForRating="select {xd.*},{r.*} from x_xprt_detail xd left join (select * from rank where rank_type=100) r on r.xprt_detail=xd.id where xd.id in "
			+ "(select xprt_detail from mfr.x_event where initiator=:user and host!=:user and start_date<now() and status in (30)) "
			+ "and xd.id not in (select xprt_detail from rank where useracc=:user)";
	private static final String queryForExisitingRating="select {xd.*},{r.*} from rank r2 , x_xprt_detail xd left join (select * from rank where rank_type=100) r on r.xprt_detail=xd.id where r2.xprt_detail=xd.id and "
			+ "r.useracc=:user and r2.rank_type=1 order by r2.create_dt desc";
	
	public List<XXprtDetail>findForUser(Integer user){
		Query result = entityManager.createNativeQuery(queryForDetail, XXprtDetail.class).setParameter("user", user);
		return result.getResultList();
	}
	public List<XXprtDetail>findForRating(Integer user){
		org.hibernate.Query query = createQuery(queryForRating,user);
		return query.list();
	}
	private org.hibernate.Query createQuery(String queryString,Integer user) {
		Session session=(Session)entityManager.getDelegate();
		org.hibernate.Query query=session.createSQLQuery(queryString)
				.addEntity("xd",XXprtDetail.class)
				.addEntity("r",Rank.class)
				.setResultTransformer(new ResultTransformer() {
					@Override
					public Object transformTuple(Object[] tuple, String[] aliases) {
						if(tuple.length==2 && tuple[1]!=null){
							((XXprtDetail) tuple[0]).setRank(((Rank)tuple[1]).getRank());
						}
						return tuple[0];
					}
					@Override
					public List transformList(List collection) {
						return collection;
					}
				});
		query.setParameter("user", user);
		return query;
	}
	public List<XXprtDetail>findForExistingRating(Integer user){
		org.hibernate.Query query = createQuery(queryForExisitingRating,user);
		return query.list();
	}
}
