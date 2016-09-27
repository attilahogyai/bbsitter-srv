package org.mfr.xprt.data;

import java.util.List;

import javax.persistence.Query;

import org.mfr.data.Address;
import org.mfr.data.AddressHome;

public class AddressDao extends AddressHome{
	public List<Address>findForUser(Integer user){
		Query result = entityManager.createNativeQuery("select * from address where useracc=:user", Address.class)
				.setParameter("user", user);
		return result.getResultList();
	}
	public Address findForAddressData(Integer city,String zip, String street, Integer user){
		Query result = entityManager.createNativeQuery("select * from address where "
				+ "useracc=:user and "
				+ "zip=:zip and "
				+ "city=:city and "
				+ "street=:street", Address.class)
				.setParameter("user", user)
				.setParameter("zip", zip)
				.setParameter("city", city)
				.setParameter("street", street);
		return (Address)result.getSingleResult();
	}
}
