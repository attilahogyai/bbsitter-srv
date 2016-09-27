package org.mfr.xprt.data;

import java.util.List;

import javax.persistence.Query;

import org.mfr.data.XProfession;
import org.mfr.data.XProfessionHome;

public class XProfessionDao extends XProfessionHome {
	@SuppressWarnings("unchecked")
	public List<XProfession> findAll() {
		try {
			Query query=entityManager.createNativeQuery("select id,name collate \"hu_HU\" from mfr.x_profession order by name", XProfession.class);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}	
}
