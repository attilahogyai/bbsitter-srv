package org.mfr.xprt.rest.controller;

import java.util.List;

import javax.persistence.PersistenceException;

import org.mfr.data.Address;
import org.mfr.data.Useracc;
import org.mfr.xprt.data.AddressDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
@Controller
public class AddressController extends AbstractBaseController<Address> {

	private static Logger log = LoggerFactory.getLogger(AddressController.class);
	protected final String NAME="address";
	@Autowired
	private AddressDao addressDao;
	
	@RequestMapping(value="/"+NAME+"/{addressId}",method = RequestMethod.GET)
	@ResponseBody
	public Object getAddress(@PathVariable Integer addressId){
		Address adrr=addressDao.findById(addressId, Address.class);
		return wrapPayload(NAME, adrr);
	}
	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getMyAddress(Authentication authentication){
		Useracc user=checkForAuth(authentication);
		List<Address> addressList=addressDao.findForUser(user.getId());
		return wrapPayload(NAME, addressList);
	}
	
	@RequestMapping(value="/"+NAME,method = RequestMethod.POST)
	@ResponseBody
	public Object createAddress(Authentication authentication,@RequestBody JsonNode address){
		Useracc user=checkForAuth(authentication);
		Address object=this.extractFromJson(address.get(NAME), Address.class);
		object.setUseracc(user);
		try{
			addressDao.persist(object);
		}catch(PersistenceException e){
			object=addressDao.findForAddressData(object.getCity().getId(),object.getZip(),object.getStreet(),object.getUseracc().getId());
		}
		return wrapPayload(NAME, object);
	}
	
	
	@Override
	protected Class getClazz() {
		return Address.class;
	}

}
