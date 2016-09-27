package org.mfr.xprt.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.Useracc;
import org.mfr.data.XXprtDetail;
import org.mfr.data.XXprtProfession;
import org.mfr.xprt.data.XSearchManagerDao;
import org.mfr.xprt.data.XXprtDetailDao;
import org.mfr.xprt.data.XXprtProfessionDao;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
@Controller
public class XprtProfessionController  extends AbstractBaseController<XXprtProfession> {

	
	@Autowired
	private XXprtProfessionDao xXprtProfessionDao;
	@Autowired
	private XXprtDetailDao xXprtDetailDao;
	@Autowired
	private XSearchManagerDao xSearchManagerDao;
	
	protected final String NAME="xprtProfession";
	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getXprtProfessions(Authentication authentication,HttpServletRequest request,
			@RequestParam(value="t",required=false) String text,
			@RequestParam(value="cId",required=false) Integer cid,
			@RequestParam(value="cN",required=false) String cityName,
			@RequestParam(value="id",required=false) Integer profId,
			@RequestParam(value="xprtid",required=false) Integer xprtId
			){
		if(profId!=null){
			XXprtProfession pr=xXprtProfessionDao.findById(profId,XXprtProfession.class);
			return wrapPayload(NAME, pr);
		}else if(xprtId!=null){
			return wrapPayload(NAME, xXprtProfessionDao.findForXprt(xprtId));
		}else if(text!=null || cid!=null || cityName!=null){
			List list=xSearchManagerDao.findXprt(text,cid,cityName);
			return wrapPayload(NAME, list);
		}
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	@RequestMapping(value="/"+NAME,method = RequestMethod.POST)
	@ResponseBody
	public Object createXprtProfession(Authentication authentication,@RequestBody JsonNode input){
		Useracc user=checkForAuth(authentication);
		
		ObjectNode xprtprofession=(ObjectNode)input.get(NAME);
		rearange(xprtprofession);
		XXprtProfession object=this.extractFromJson(xprtprofession, XXprtProfession.class);
		XXprtDetail xprtDetail=(XXprtDetail)xXprtDetailDao.findByIdG(object.getXprtDetail().getId(),XXprtDetail.class);
		object.setXprtDetail(xprtDetail);
		checkOwner(authentication, object.getXprtDetail().getUseracc());
		xXprtProfessionDao.persist(object);
		return wrapPayload(NAME, object);
	}
	
	@RequestMapping(value="/"+NAME+"/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateXprtProfession(@PathVariable Integer id,@RequestBody JsonNode inputJson,Authentication authentication){
		checkForAuth(authentication);
		ObjectNode xprtprofession=(ObjectNode)inputJson.get(NAME);
		rearange(xprtprofession);
		XXprtProfession source=this.extractFromJson(xprtprofession, XXprtProfession.class);
		XXprtProfession detail=xXprtProfessionDao.findById(id,XXprtProfession.class);
		checkOwner(authentication, detail.getXprtDetail().getUseracc());
		BeanUtils.copyProperties(source, detail,getIgnorePropertiesForPut("xprtdetail"));
		xXprtProfessionDao.evict(detail.getXprtDetail());
		xXprtProfessionDao.merge(detail);
		return wrapPayload(NAME, detail);
	}
	@RequestMapping(value="/"+NAME+"/{id}",method = RequestMethod.DELETE)
	@ResponseBody
	public Object deleteXprtProfession(@PathVariable Integer id,Authentication authentication){
		checkForAuth(authentication);
		XXprtProfession detail=xXprtProfessionDao.findById(id,XXprtProfession.class);
		checkOwner(authentication, detail.getXprtDetail().getUseracc());
		xXprtProfessionDao.remove(detail);
		return wrapPayload(NAME, detail);
	}
	private void rearange(ObjectNode xprtprofession) {
		if(xprtprofession.has("xprtdetail")){
			xprtprofession.put("xprtDetail", xprtprofession.get("xprtdetail"));
			xprtprofession.remove("xprtdetail");
		}
	}

	
	
	
	@Override
	protected Class getClazz() {
		return XXprtProfession.class;
	}

}
