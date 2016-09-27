package org.mfr.xprt.data;

import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mfr.data.XLangtext;
import org.mfr.data.XLangtextHome;

public class XLangtextDao extends XLangtextHome {
	static Log log = LogFactory.getLog(XLangtextDao.class);
	private XLangtext getText(String type, String code, String lang){
			XLangtext langtext = (XLangtext) entityManager
				.createQuery(
						"from XLangtext where type=:type and code=:code and language=:lang")
				.setParameter("type", type)
				.setParameter("code", code)
				.setParameter("lang", lang)
				.getSingleResult();
			return langtext;
	}
	public String getTextValue(String type, String code, String lang){
		try{
			XLangtext t=this.getText(type, code, lang);
			return t.getText();
		}catch(NoResultException e){
			log.warn("text not found for:"+type+"/"+code+"/"+lang);
			return "/"+type+"/"+code+"/"+lang;
		}		
	}
}
