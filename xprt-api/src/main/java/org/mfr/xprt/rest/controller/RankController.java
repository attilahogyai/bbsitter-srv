package org.mfr.xprt.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mfr.data.Comment;
import org.mfr.data.Rank;
import org.mfr.data.Useracc;
import org.mfr.data.XXprtDetail;
import org.mfr.xprt.data.CommentDao;
import org.mfr.xprt.data.RankDao;
import org.mfr.xprt.rest.exception.CheckException;
import org.mfr.xprt.rest.exception.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
@Controller
public class RankController extends AbstractBaseController<Rank> {
	private static Logger log = LoggerFactory.getLogger(RankController.class);
	protected final String NAME="rank";
	@Autowired
	private RankDao rankDao;
	
	
	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getRanks(Authentication authentication,
			@RequestParam(required=true,value="xprt") Integer xprtId,
			@RequestParam(required=false,value="user") Integer userId ){
		this.addSerializeSkipClass(XXprtDetail.class);
		if(userId==null){ // find average 
			List rankList=rankDao.findAvgByXprt(xprtId);
			return wrapPayload(NAME, rankList);
		}else{ // find ranks created by user
			List rankList=rankDao.findByXprtAndUser(xprtId, userId);
			return wrapPayload(NAME, rankList);
		}
	}	
	
	@RequestMapping(value="/"+NAME,method = RequestMethod.POST)
	@ResponseBody
	public Object createRank(Authentication authentication,@RequestBody JsonNode event){
		Useracc user=checkForAuth(authentication);
		Rank object=this.extractFromJson(event.get(NAME), Rank.class);
		object.setUseracc(user);
		if(object.getRank()>5){
			throw new CheckException("rank should be between 1-5 inclusive");
		}
		rankDao.persist(object);
		
		createAvgRanks(user, object, object);
		
		Map m=new HashMap();
		mergeObjectToMap(m,object,true);
		return m;
	}

	
	
	@RequestMapping(value="/"+NAME+"/{rankid}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateRank(@PathVariable Integer rankid, Authentication authentication,@RequestBody JsonNode event){
		Useracc user=checkForAuth(authentication);
		Rank object=this.extractFromJson(event.get(NAME), Rank.class);
		Rank oldRank=rankDao.findById(rankid,Rank.class);
		if(oldRank==null){
			throw new NotFound("object not found");
		}
		checkOwner(authentication, oldRank.getUseracc());

		if(object.getRank()>5){
			throw new CheckException("rank should be between 1-5 inclusive");
		}
		oldRank.setRank(object.getRank());
		
		oldRank.setComment(object.getComment());
		rankDao.merge(oldRank);
		
		createAvgRanks(user, object, oldRank);
		Map m=new HashMap();
		mergeObjectToMap(m,oldRank,true);
		return m;
	}

	private void createAvgRanks(Useracc user, Rank object, Rank oldRank) {
		// TODO create new Rank and save
		Object[] avgRank=(Object[])rankDao.calcAvgXprt(oldRank.getXprtDetail().getId(),oldRank.getRankType());
		
		List<Rank> avgRankObj=rankDao.findAvgByXprt(oldRank.getXprtDetail().getId(),oldRank.getRankType()*10);
		Rank avg=null;
		if(avgRankObj.size()>1){
			log.warn("ha more than one rank object xprt:"+object.getXprtDetail().getId()+" type:"+object.getRankType()*10);
		}else if(avgRankObj.size()==0){
			avg=new Rank();
		}else{
			avg=avgRankObj.get(0);
		}
		avg.setRankType(object.getRankType()*10);
		avg.setXprtDetail(object.getXprtDetail());
		avg.setUseracc(user);
		if(avgRank==null){
			avg.setRank(object.getRank());
		}else{
			avg.setRank(Float.valueOf(avgRank[2].toString()));
		}
		rankDao.merge(avg);
		
		int retry = 2;
		boolean exception=false;
		do{
			Object [] totalAvgResult=(Object[])rankDao.calcTotalAvg(object.getXprtDetail().getId());
			List<Rank> totalRankObj=rankDao.findAvgByXprt(object.getXprtDetail().getId(),100);
			Rank totalAvg=null;
			if(totalRankObj.size()>1){
				log.warn("has more than one rank object xprt:"+object.getXprtDetail().getId()+" type: 100");
			}else if(totalRankObj.size()==0){
				totalAvg=new Rank();
			}else{
				totalAvg=totalRankObj.get(0);
			}
			
			totalAvg.setRankType(100);
			totalAvg.setXprtDetail(object.getXprtDetail());
			totalAvg.setUseracc(user);
			totalAvg.setRank(Float.valueOf(totalAvgResult[1].toString()));
			
			try{
				rankDao.merge(totalAvg);
			}catch(Exception e){
				exception=true;
			}
			retry--;
		}while(retry>0 && exception);
		log.debug("rank created");
	}
	
	
	@Override
	protected Class getClazz() {
		return Rank.class;
	}

}
