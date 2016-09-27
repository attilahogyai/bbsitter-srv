package org.mfr.xprt.data;

import java.util.List;

import javax.persistence.Query;

import org.mfr.data.City;
import org.mfr.data.CityHome;


public class CityDao extends CityHome {
	private static final String queryForLocation="select country_code,timezoneid,state_code,city_name,min(id) as id from mfr.weblocations where country_code=:ccode and city_name ilike :cname group by country_code,timezoneid,state_code,city_name order by city_name";
	
	public List<City>findByCountryAndBegin(String country, String begin){
		Query result = entityManager.createNativeQuery(queryForLocation, City.class)
				.setParameter("ccode", country)
				.setParameter("cname", begin+'%');
		return result.getResultList();
	}
}
