package org.mfr.rest.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mfr.data.Address;
import org.mfr.data.Category;
import org.mfr.data.City;
import org.mfr.data.Comment;
import org.mfr.data.Country;
import org.mfr.data.NationalHoliday;
import org.mfr.data.Rank;
import org.mfr.data.UserPreference;
import org.mfr.data.Useracc;
import org.mfr.data.XEvent;
import org.mfr.data.XLanguage;
import org.mfr.data.XLog;
import org.mfr.data.XProfession;
import org.mfr.data.XSetup;
import org.mfr.data.XXprtDetail;
import org.mfr.data.XXprtProfession;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Component
public class MfrObjectMapper extends ObjectMapper {
	private static final long serialVersionUID = 9045197387563477844L;
	private static Logger log = Logger.getLogger(MfrObjectMapper.class);
	public static List<Class> hanledObjects=new ArrayList<Class>();
	static{
		hanledObjects.add(XEvent.class);
		hanledObjects.add(XSetup.class);
		hanledObjects.add(XXprtProfession.class);
		hanledObjects.add(UserPreference.class);
		hanledObjects.add(Useracc.class);
		hanledObjects.add(XXprtDetail.class);
		hanledObjects.add(XProfession.class);
		hanledObjects.add(Country.class);
		hanledObjects.add(City.class);
		hanledObjects.add(NationalHoliday.class);
		hanledObjects.add(Comment.class);
		hanledObjects.add(Rank.class);
		hanledObjects.add(Address.class);
		hanledObjects.add(XLog.class);
		
		// MFR objects
		hanledObjects.add(Category.class);
		EmberStyleModelSerializer.simpleSet.addAll(hanledObjects);
	}

	public MfrObjectMapper() {
		SimpleModule module=new SimpleModule();
		EmberStyleModelSerializer serializer=new EmberStyleModelSerializer();
		for (Class clazz : hanledObjects) {
			module.addSerializer(clazz, serializer);
		}
		
		this.registerModule(new Hibernate4Module());
		this.registerModule(module);
		this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Override
	public <T> T readValue(JsonParser jp, Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		return super.readValue(jp, valueType);
	}

	@Override
	public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		return super.readValue(jp, valueTypeRef);
	}

	@Override
	public <T> T readValue(JsonParser jp, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		return super.readValue(jp, valueType);
	}

	protected Object _readMapAndClose(JsonParser jp, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		try {
			Object result;
			JsonToken t = _initForReading(jp);
			if (t == JsonToken.VALUE_NULL) {
				// [JACKSON-643]: Ask JsonDeserializer what 'null value' to use:
				DeserializationContext ctxt = createDeserializationContext(jp,
						getDeserializationConfig());
				result = _findRootDeserializer(ctxt, valueType).getNullValue();
			} else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
				result = null;
			} else {
				DeserializationConfig cfg = getDeserializationConfig();
				DeserializationContext ctxt = createDeserializationContext(jp,
						cfg);
				JsonDeserializer<Object> deser = _findRootDeserializer(ctxt,
						valueType);
				if (cfg.useRootWrapping()) {
					result = _unwrapAndDeserialize(jp, ctxt, cfg, valueType,
							deser);
				} else {
					result = deser.deserialize(jp, ctxt);
				}
			}
			// Need to consume the token too
			jp.clearCurrentToken();
			return result;
		} finally {
			try {
				jp.close();
			} catch (IOException ioe) {
			}
		}
	}
	
}
