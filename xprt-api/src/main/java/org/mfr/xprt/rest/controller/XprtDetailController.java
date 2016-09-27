package org.mfr.xprt.rest.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.Useracc;
import org.mfr.data.XXprtDetail;
import org.mfr.xprt.data.XSearchManagerDao;
import org.mfr.xprt.data.XXprtDetailDao;
import org.mfr.xprt.rest.exception.CheckException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class XprtDetailController extends AbstractBaseController<XXprtDetail> {
	private static Logger log = LoggerFactory.getLogger(XprtDetailController.class);
	@Autowired
	private XSearchManagerDao xSearchManagerDao;

	@Autowired
	private XXprtDetailDao xXprtDetailDao; 
	
	protected final String NAME="xprtDetail";
	
	

	@RequestMapping(value="/"+NAME+"/{id}",method = RequestMethod.GET)
	@ResponseBody
	public Object getXprtDetailById(Authentication authentication,@PathVariable Integer id,@RequestParam(value="useraccId",required=false) Integer useraccId){
		if(id==0) { // return own record
			Useracc user=checkForAuth(authentication);
			List<XXprtDetail> detailList=null;
			if(useraccId!=null){
				detailList=xXprtDetailDao.findForUser(useraccId);
			}else{
				detailList=xXprtDetailDao.findForUser(user.getId());
			}
			if(detailList.size()>1){
				throw new CheckException("id=0 not support for multimple results");
			}else if(detailList.size()==1){
				return wrapPayload(NAME, detailList.get(0));
			}else{
				return wrapPayload(NAME, null);
			}
		}
		return wrapPayload(NAME, xXprtDetailDao.findById(id,XXprtDetail.class));
	}	
	
	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getXprtDetail(Authentication authentication,HttpServletRequest request,
			@RequestParam(value="id",required=false) Integer id,
			@RequestParam(value="useraccId",required=false) Integer useraccId,
			@RequestParam(value="offer",required=false) Boolean offer,
			@RequestParam(value="cId",required=false) Integer cid,
			@RequestParam(value="cN",required=false) String cityName,
			@RequestParam(value="forRating",required=false) Boolean forRating,
			@RequestParam(value="direction",required=false) String direction
			){
		if(forRating!=null && forRating){ // get xprtdetail ist for rating
			Useracc user=checkForAuth(authentication);
			List<XXprtDetail> list =null;
			if(direction.equals("next")){
				list = xXprtDetailDao.findForRating(user.getId());
			}else{
				list = xXprtDetailDao.findForExistingRating(user.getId());
			}
			Set unique=new LinkedHashSet();
			for (XXprtDetail object : list) {
				unique.add(object);
			}
			return wrapPayload(NAME, unique);
		}else{
			if(id!=null){
				XXprtDetail detail=xXprtDetailDao.findById(id,XXprtDetail.class);
				log.debug("detail:"+detail);
				return wrapPayload(NAME, detail);
			}else if((offer!=null && offer) || cid!=null || cityName!=null){
				addSerializeSkipClass(Useracc.class);
				List list=null;
				if(offer!=null){
					list=xSearchManagerDao.topXprt(4); 
				}else{
					list=xSearchManagerDao.findXprt(null , cid,cityName,false,null);
				}
				return wrapPayload(NAME, list);
			}else if(useraccId!=null){
				return wrapPayload(NAME, xXprtDetailDao.findForUser(useraccId));
			}else{
				Useracc user=checkForAuth(authentication);
				return wrapPayload(NAME, xXprtDetailDao.findForUser(user.getId()));
			}
		}
	}
		
	
	@RequestMapping(value="/"+NAME,method = RequestMethod.POST)
	@ResponseBody
	public Object createXprtDetail(Authentication authentication,@RequestBody JsonNode event){
		
		XXprtDetail object=this.extractFromJson(event.get(NAME), XXprtDetail.class);
		object.setUseracc((Useracc)authentication.getPrincipal());
		object.setCreateDt(new Date());
		xXprtDetailDao.persist(object);
		Map m=new HashMap();
		
		mergeObjectToMap(m,object,true);
		return m;
	}
	
	@RequestMapping(value="/"+NAME+"/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateXprtDetail(@PathVariable Integer id,@RequestBody JsonNode eventJson,Authentication authentication){
		Useracc user=checkForAuth(authentication);
		XXprtDetail source=this.extractFromJson(eventJson.get(NAME), XXprtDetail.class);
		XXprtDetail detail=null;
		if(id==0) { // return own record
			List<XXprtDetail> detailList=xXprtDetailDao.findForUser(user.getId());
			if(detailList.size()>1){
				throw new CheckException("id=0 not support for multimple results");
			}
			detail=detailList.get(0);
		}else{
			detail=xXprtDetailDao.findById(id,XXprtDetail.class);
		}
		
		if(detail!=null && !detail.getUseracc().getId().equals(checkForAuth(authentication).getId())){
			return new ResponseEntity<String>(YOU_ARE_NOT_OWNER_OF_OBJECT,HttpStatus.FORBIDDEN);
		}
		BeanUtils.copyProperties(source, detail,getIgnorePropertiesForPut("useracc","top","createDt"));
		xXprtDetailDao.evict(detail.getUseracc());
		xXprtDetailDao.merge(detail);
		Map m=new HashMap();
		detail.setId(id);
		mergeObjectToMap(m,detail,true);
		return m;
	}

	
	@Override
	protected Class getClazz() {
		return XXprtDetail.class;
	}

}
