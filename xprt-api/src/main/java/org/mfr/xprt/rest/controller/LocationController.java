package org.mfr.xprt.rest.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.Country;
import org.mfr.xprt.data.CountryDao;
import org.mfr.xprt.data.CityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LocationController extends AbstractBaseController {
	@Autowired
	private CountryDao countryDao;
	
	@Autowired
	private CityDao cityDao;
	
	@RequestMapping(value="/country",method = RequestMethod.GET)
	@ResponseBody
	public Object getAllCountry(HttpServletRequest request){
		Map result=wrapPayload("country", countryDao.findAll(Country.class));
		return result;
	}
	@RequestMapping(value="/ip",method = RequestMethod.GET)
	@ResponseBody
	public Object getClientIP(HttpServletRequest request){
		return fillClientRequestData(request);
	}
	@RequestMapping(value="/city",method = RequestMethod.GET)
	@ResponseBody
	public Object getFilteredCity(@RequestParam(value="begin",required=true) String begin,@RequestParam(value="country",required=true) String countryCode,HttpServletRequest request){
		if(countryCode==null || begin.length()<3){
			// return empty result
			return wrapPayload("city", new ArrayList());
		}
		Map result=wrapPayload("city", cityDao.findByCountryAndBegin(countryCode,begin));
		return result;
	}
	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return null;
	}

}
