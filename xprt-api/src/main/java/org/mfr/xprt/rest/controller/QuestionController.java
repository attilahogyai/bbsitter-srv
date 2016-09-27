package org.mfr.xprt.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.Question;
import org.mfr.data.XProfession;
import org.mfr.xprt.data.QuestionDao;
import org.mfr.xprt.data.XProfessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuestionController extends AbstractBaseController<Question>{

	protected final String NAME="question";
	@Autowired
	private QuestionDao questionDao;
	

	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getAll(HttpServletRequest request){
		return wrapPayload(NAME, questionDao.findAll(Question.class));
	}
	@RequestMapping(value="/"+NAME+"/{professionId}",method = RequestMethod.GET)
	@ResponseBody
	public Object get(@PathVariable Integer professionId,HttpServletRequest request){
		return wrapPayload(NAME, questionDao.findByIdG(professionId,XProfession.class));
	}
	@Override
	protected Class getClazz() {
		return Question.class;
	}

}
