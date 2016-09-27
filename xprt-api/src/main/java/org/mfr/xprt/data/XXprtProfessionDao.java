package org.mfr.xprt.data;

import java.util.List;

import javax.persistence.Query;

import org.mfr.data.XXprtProfession;
import org.mfr.data.XXprtProfessionHome;

public class XXprtProfessionDao extends XXprtProfessionHome {
	public List<XXprtProfession>findForXprt(Integer xprtdetail){
		Query result = entityManager.createQuery("from XXprtProfession where xprtDetail.id=:xprtdetail").setParameter("xprtdetail", xprtdetail);
		return result.getResultList();
	}
}
