package org.mfr.rest.serializer;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.mfr.data.Address;
import org.mfr.data.City;
import org.mfr.data.Comment;
import org.mfr.data.Country;
import org.mfr.data.Hidden;
import org.mfr.data.Sensible;
import org.mfr.data.Useracc;
import org.mfr.data.UseraccPrefs;
import org.mfr.data.XEvent;
import org.mfr.data.XLanguage;
import org.mfr.data.XLog;
import org.mfr.data.XProfession;
import org.mfr.data.XXprtDetail;
import org.mfr.xprt.rest.controller.AbstractBaseController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class EmberStyleModelSerializer extends JsonSerializer {
	private static Logger log = Logger.getLogger(EmberStyleModelSerializer.class);
	private PropertyUtilsBean propertyUtils=new PropertyUtilsBean();
	public static final Set<Class> simpleSet=new HashSet<Class>();
	static{
		simpleSet.add(String.class);
		simpleSet.add(Date.class);
		simpleSet.add(Timestamp.class);
		simpleSet.add(java.util.Date.class);
		simpleSet.add(java.lang.Integer.class);
		simpleSet.add(java.lang.Long.class);
		simpleSet.add(String[].class);
		simpleSet.add(Integer[].class);
		simpleSet.add(Float.class);
		
		simpleSet.add(XLanguage.class);
	}
	private static final Set<Class> hiddenSet=new HashSet<Class>();
	static{
		hiddenSet.add(XEvent.class);
		hiddenSet.add(Useracc.class);
	}
	private static final Set<Class> evictionSet=new HashSet<Class>();
	static{
		evictionSet.add(XEvent.class);
		evictionSet.add(Useracc.class);
		evictionSet.add(XXprtDetail.class);
	}
	protected void serializeSimpleProperties(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws JsonGenerationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException{
		
        PropertyDescriptor[] properties=PropertyUtils.getPropertyDescriptors(value.getClass());
        boolean justId=AbstractBaseController.isSeralizeJustId();
    	boolean serializeHidden=AbstractBaseController.isSerializeHidden();
    	if(serializeHidden && hiddenSet.contains(value.getClass())){
    		serializeHidden=!((Sensible)value).isSensible();
    	}
    	log.debug("serialize ["+value.getClass()+"] justId["+justId+"]");
        for (int i = 0; i < properties.length; i++) {
        	Class type=properties[i].getPropertyType();
        	Object v=propertyUtils.getProperty(value, properties[i].getName());
        	
        	if(v==null){
        		jgen.writeNullField(properties[i].getName());
        		continue;
        	}
        	if(!serializeHidden){
        		try {
					Hidden hidden=value.getClass().getDeclaredField(properties[i].getName()).getAnnotation(Hidden.class);
					if(hidden!=null) continue;
				} catch (Exception e) {
					continue;
				}
        	}
    		if(simpleSet.contains(type)){
        		if(type.equals(String.class)){
        			jgen.writeStringField(properties[i].getName(), (String)v);
        		}else if(type.equals(Integer.class)){
        			jgen.writeNumberField(properties[i].getName(), (Integer)v);
        		}else if(type.equals(Float.class)){
        			jgen.writeNumberField(properties[i].getName(), (Float)v);
        		}else if(type.equals(Integer[].class)){
        			Integer [] iarray=(Integer[])v;
        			jgen.writeArrayFieldStart(properties[i].getName());
        			for (int j = 0; j < iarray.length; j++) {
        				jgen.writeNumber(iarray[j]);
					}
        			jgen.writeEndArray();
        		}else if(type.equals(String[].class)){
        			String [] iarray=(String[])v;
        			jgen.writeArrayFieldStart(properties[i].getName());
        			for (int j = 0; j < iarray.length; j++) {
        				jgen.writeString(iarray[j]);
					}
        			jgen.writeEndArray();        			        			
        		}else if(type.equals(Useracc.class)){
        			jgen.writeNumberField(properties[i].getName(), ((Useracc)v).getId());
        		}else if(type.equals(XXprtDetail.class) && justId){
        			jgen.writeNumberField(properties[i].getName(), ((XXprtDetail)v).getId());
        			
        		}else if(type.equals(XProfession.class) && justId){
        			jgen.writeNumberField(properties[i].getName().toLowerCase(), ((XProfession)v).getId());
        		}else if(type.equals(XLanguage.class) && justId){
        			jgen.writeNumberField(properties[i].getName().toLowerCase(), ((XLanguage)v).getId());
        		}else if(type.equals(City.class) && justId){
        			jgen.writeNumberField(properties[i].getName().toLowerCase(), ((City)v).getId());
        		}else if(type.equals(Country.class) && justId){
        			jgen.writeStringField(properties[i].getName().toLowerCase(), ((Country)v).getId());        			
        		}else if(type.equals(UseraccPrefs.class)){
        			// skip
        		}else if(type.equals(Address.class)){
        			jgen.writeNumberField(properties[i].getName().toLowerCase(), ((Address)v).getId());
        		}else if(type.equals(Comment.class)){
        			jgen.writeNumberField(properties[i].getName().toLowerCase(), ((Comment)v).getId());
        		}else{
        			jgen.writeObjectField(properties[i].getName(), v);
        		}
        	}
		}
	}
	
	@Override
	public void serialize(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		try {
			serializeSimpleProperties(value, jgen, provider);
		} catch (Exception e) {
			log.error("error",e);
		}
        jgen.writeEndObject();
	}
}
