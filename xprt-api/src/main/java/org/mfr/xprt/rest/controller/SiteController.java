package org.mfr.xprt.rest.controller;

import org.mfr.data.PhotoCategoryDao;
import org.mfr.data.Site;
import org.mfr.data.SiteGalleriesDao;
import org.springframework.beans.factory.annotation.Autowired;

public class SiteController extends AbstractBaseController<Site> {
	@Autowired
	private SiteGalleriesDao siteGalleriesDao;
	@Autowired
	private PhotoCategoryDao photoCategoryDao;	
	
	
	
	@Override
	protected Class getClazz() {
		return Site.class;
	}
	
}
