package org.mfr.xprt.rest.controller;

import org.mfr.xprt.data.XLangtextDao;
import org.springframework.beans.factory.annotation.Autowired;

public class XLangtextDaoHelper {
	@Autowired
	private XLangtextDao langtextDao;
	public String getText(String group, String code, String lang){
		return langtextDao.getTextValue(group, code, lang.toLowerCase());
	}
}
