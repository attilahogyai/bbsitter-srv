package org.mfr.xprt.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.mfr.data.AbstractDao;
import org.mfr.data.Rank;
import org.mfr.data.XProfession;
import org.mfr.data.XXprtDetail;
import org.mfr.data.XXprtProfession;
import org.mfr.xprt.rest.controller.XprtDetailController;

public class XSearchManagerDao extends AbstractDao{
	private static final Log log = LogFactory.getLog(XSearchManagerDao.class);
	@SuppressWarnings("unchecked")
	public List<Object> findXprt(String text,Integer cId,String cityName) {
		return findXprt(text,cId,cityName,false,null);
	}
//	public List<Object> topXprt(int count) {
//		Session session=(Session)entityManager.getDelegate();
//		String search="select distinct e.xprt_detail,max(e.id) as last from x_event e join x_xprt_detail xd on  xd.id=e.xprt_detail "
//				+ "and xd.status=1 group by e.xprt_detail order by last desc limit "+count;
//		Query query=session.createSQLQuery(search);
//		List<Object> result=query.list();
//		
//		List<Integer> ids=new ArrayList<Integer>();
//		if(result.size()==0){ // search top
//			query=session.createSQLQuery("select id from x_xprt_detail where status=1 order by top,id desc limit "+count);
//			result=query.list();
//			for (Object object : result) {
//				ids.add((Integer)object);
//			}			
//		}else{
//			
//			for (Object object : result) {
//				ids.add((Integer)((Object[]) object)[0]);
//			}
//		}
//		log.debug("ids count["+result.size()+"]");
//		return findXprt(null,null,null,false,ids);
//	}
	public List<Object> topXprt(int count) {
		Session session = (Session) entityManager.getDelegate();
//		String search = "select distinct e.xprt_detail,max(e.id) as last from x_event e join x_xprt_detail xd on  xd.id=e.xprt_detail "
//				+ "and xd.status=1 group by e.xprt_detail order by last desc limit "
//				+ count;
//		Query query = session.createSQLQuery(search);
//		List<Object> result = query.list();
//
		List<Integer> ids = new ArrayList<Integer>();
//		if (result.size() == 0) { // search top
		Query query = session
					.createSQLQuery("select id from x_xprt_detail where status=1 order by top,id desc limit "
							+ count);
		List<Object> result = query.list();
			for (Object object : result) {
				ids.add((Integer) object);
			}
//		} else {
//
//			for (Object object : result) {
//				ids.add((Integer) ((Object[]) object)[0]);
//			}
//		}
		log.debug("ids count[" + result.size() + "]");
		return findXprt(null, null, null, false, ids);
	}
	
	public List<Object> findXprt(String text,Integer cId,String cityName,boolean top,List<Integer> ids) {
		log.debug("findXprt ["+text+"] cId["+cId+"] cityName["+cityName+"]");
		try {
			Session session=(Session)entityManager.getDelegate();
			String search="";
			boolean textSearch=false;
			if(text!=null || cityName!=null){
				if(text==null || text.equals("")){
					text=cityName;
				}
				search="(xd.location_str ilike :text or "
					+ "xd.name ilike :text or "
					+ "xd.description ilike :text or "
					+ "x_p.name ilike :text ) or "
					+ "((select array_agg(id) from mfr.weblocations where city_name ilike :text and country_code='HU') && (xd.target_locations))";
				textSearch=true;
			}
			if(top){
				search+="xd.top=1";
			}
			if(ids!=null){
				StringBuffer sb=new StringBuffer("xd.id in (");  
				for (Integer id : ids) {
					sb.append(id).append(",");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append(")");
				search=sb.toString();				
			}
			boolean citySearch=false;
			if(cId!=null && cId>0){
				search+=search.equals("")?"( :location = any (xd.target_locations) )":" or ( :location = any (xd.target_locations) )";
				citySearch=true;
			}
			Query query=session.createSQLQuery("select {xd.*}, {x_p.*}, {r.*} "
					+ "from x_xprt_detail xd left join rank r on xd.id=r.xprt_detail and r.rank_type=100, x_profession x_p "
					+ "where xd.status=1 and xd.profession=x_p.id "
					+ "and ("+search+") order by xd.id desc")
					.addEntity("xd",XXprtDetail.class)
					.addEntity("x_p",XProfession.class)
					.addEntity("r",Rank.class)
					.setResultTransformer(new ResultTransformer() {
						@Override
						public Object transformTuple(Object[] tuple, String[] aliases) {
							if(tuple[2]!=null){
								((XXprtDetail) tuple[0]).setRank(((Rank)tuple[2]).getRank());
							}
							return tuple[0];
						}
						@Override
						public List transformList(List collection) {
							return collection;
						}
					});
			if(textSearch){
				query.setParameter("text", ('%'+text+'%'));
			}
			if(citySearch){
				query.setParameter("location", cId);
			}					
			List<Object> result=query.list();
			log.debug("result count["+result.size()+"]");
			return result;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}	
	
}
