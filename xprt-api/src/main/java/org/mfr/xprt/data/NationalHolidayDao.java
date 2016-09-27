package org.mfr.xprt.data;

import java.util.List;

import javax.persistence.Query;

import org.mfr.data.NationalHoliday;
import org.mfr.data.NationalHolidayHome;

public class NationalHolidayDao extends NationalHolidayHome {
	public List<NationalHoliday>findForCountry(String  countryCode){
		Query result = entityManager.createQuery("from NationalHoliday where country.id=:cid").setParameter("cid", countryCode);
		return result.getResultList();
	}

}
