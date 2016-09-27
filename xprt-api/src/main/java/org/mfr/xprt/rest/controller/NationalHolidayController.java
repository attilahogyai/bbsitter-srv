package org.mfr.xprt.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.NationalHoliday;
import org.mfr.xprt.data.NationalHolidayDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class NationalHolidayController extends AbstractBaseController<NationalHoliday> {
	protected final String NAME="nationalHoliday";
	@Autowired
	private NationalHolidayDao nationalHolidayDao;
	

	@RequestMapping(value="/"+NAME,method = RequestMethod.GET)
	@ResponseBody
	public Object getForCountry(HttpServletRequest request,@RequestParam(required=true,value="countryCode") String countryId){
		return wrapPayload(NAME, nationalHolidayDao.findForCountry(countryId));
	}
	@RequestMapping(value="/"+NAME+"/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateEvents(@PathVariable Integer id,@RequestBody JsonNode eventJson,Authentication authentication){
		NationalHoliday source=this.extractFromJson(eventJson.get(NAME), NationalHoliday.class);
		NationalHoliday nationalHoliday=nationalHolidayDao.findById(id,NationalHoliday.class);
		if(nationalHoliday==null){
			return new ResponseEntity<String>("object not found",HttpStatus.NOT_FOUND);
		}
		BeanUtils.copyProperties(source, nationalHoliday,getIgnorePropertiesForPut());
		nationalHolidayDao.merge(nationalHoliday);
		return wrapPayload(NAME, nationalHoliday);
	}
	
	@RequestMapping(value = "/" + NAME, method = RequestMethod.POST)
	@ResponseBody
	public Object create(Authentication authentication, @RequestBody JsonNode event) {
		NationalHoliday object=this.extractFromJson(event.get(NAME), NationalHoliday.class);
		nationalHolidayDao.persistG(object);
		return wrapPayload(NAME, object);
	}
	
	@Override
	protected Class getClazz() {
		return NationalHoliday.class;
	}

}
