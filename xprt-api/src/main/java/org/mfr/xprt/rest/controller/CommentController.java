package org.mfr.xprt.rest.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.Comment;
import org.mfr.data.Useracc;
import org.mfr.data.XXprtDetail;
import org.mfr.rest.util.MailHelper;
import org.mfr.xprt.data.CommentDao;
import org.mfr.xprt.data.UseraccDao;
import org.mfr.xprt.rest.exception.CheckException;
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
public class CommentController extends AbstractBaseController<Comment> {

	private static Logger log = LoggerFactory.getLogger(CommentController.class);
	protected final String NAME="comment";
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private UseraccDao useraccDao;
	
	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getComments(Authentication authentication,
			@RequestParam(required=false,value="xprt") Integer xprtId,
			@RequestParam(required=false,value="folder") String folder){
		if(xprtId!=null){
			List commentList=commentDao.findOpinionsForXprt(xprtId);
			return wrapPayload(NAME, commentList);
		}else if(folder!=null && folder.equals("in")){
			Useracc user=checkForAuth(authentication);
			List<Comment> commentList=commentDao.findMessagesForInbox(user.getId());
			for (Comment comment : commentList) {
				if(comment.getStatus()==1){
					comment.setStatus(2);
					commentDao.merge(comment);
					comment.setStatus(1);
				}
			}
			return wrapPayload(NAME, commentList);
		}else if(folder!=null && folder.equals("out")){
			Useracc user=checkForAuth(authentication);
			List commentList=commentDao.findMessagesForOutbox(user.getId());
			return wrapPayload(NAME, commentList);
		}
		return null;
	}	
	
	@RequestMapping(value="/"+NAME,method = RequestMethod.POST)
	@ResponseBody
	public Object createComment(HttpServletRequest request,Authentication authentication,@RequestBody JsonNode event){
		Useracc user=checkForAuth(authentication);
		Comment object=this.extractFromJson(event.get(NAME), Comment.class);
		object.setUseracc(user);
		object.setCreateDt(new Date());
		if(object.getStatus()==null){
			object.setStatus(1);
		}
		Useracc addressee=object.getAddressee();
		if(addressee==null && object.getXprtDetail()!=null){
			object.setXprtDetail((XXprtDetail)commentDao.findByIdG(object.getXprtDetail().getId(), XXprtDetail.class));
			addressee=object.getXprtDetail().getUseracc();
		}
		if(object.getUseracc().getId().equals(addressee.getId())){
			throw new CheckException("user and addressee matches not allowed");
		}
		addressee=useraccDao.findById(addressee.getId(),Useracc.class);
		if(addressee==null){
			throw new CheckException("addressee not found in database");
		}
		object.setAddressee(addressee);
		commentDao.persist(object);
		if(object.getSource()!=null && object.getSource()!=1){ // do not send email in case of ranking
			Map additionArgs=new HashMap();
			additionArgs.put("comment", object);
			sendEmailAsync(getLocale(request), MailHelper.NEW_COMMENT, object.getAddressee().getEmail(), additionArgs);
		}
		Map m=new HashMap();
		mergeObjectToMap(m,object,true);
		return m;
	}
	@RequestMapping(value="/"+NAME+"/{commentid}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateComment(@PathVariable Integer commentid,Authentication authentication,@RequestBody JsonNode event){
		Useracc user=checkForAuth(authentication);
		Comment object=this.extractFromJson(event.get(NAME), Comment.class);
		Comment oldObject=commentDao.findById(commentid);
		checkOwner(authentication, oldObject.getUseracc());
		object.setUseracc((Useracc)authentication.getPrincipal());
		oldObject.setComment(object.getComment());
		commentDao.merge(oldObject);
		Map m=new HashMap();
		mergeObjectToMap(m,oldObject,true);
		return m;
	}
	@Override
	protected Class getClazz() {
		return Comment.class;
	}
}
