package org.mfr.xprt.rest.controller;

import org.mfr.data.Category;
import org.mfr.data.CategoryDao;
import org.mfr.data.Useracc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class CategoryController extends AbstractBaseController<Category> {
	protected final String NAME="album";
	@Autowired
	private CategoryDao categoryDao;
	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getMyAlbums(@RequestParam(required=true,value="owner") boolean owner,Authentication auth){
		Useracc user=checkForAuth(auth);
		if(owner){
			return wrapPayload(NAME, categoryDao.findByUseracc(user));
		}
		return null;
	}
	@Override
	protected Class getClazz() {
		return Category.class;
	}

}
