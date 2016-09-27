package org.mfr.rest.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

public class MfrJacksonSerializerFactory extends BeanSerializerFactory {

	private static final long serialVersionUID = -9210935534933171374L;
	
	protected MfrJacksonSerializerFactory(SerializerFactoryConfig config) {
		super(config);
	}

	@Override
	public JsonSerializer<Object> createSerializer(SerializerProvider prov,
			JavaType baseType) throws JsonMappingException {
		return createSerializer(prov,baseType);
	}

	@Override
	public SerializerFactory withConfig(SerializerFactoryConfig config) {
		// TODO Auto-generated method stub
		return super.withConfig(config);
	}
   
}
