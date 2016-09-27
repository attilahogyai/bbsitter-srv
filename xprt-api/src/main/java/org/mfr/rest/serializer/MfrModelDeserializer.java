package org.mfr.rest.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
//http://www.baeldung.com/jackson-deserialization
public class MfrModelDeserializer extends JsonDeserializer {

	@Override
	public Object deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
//		        JsonNode node = jp.getCodec().readTree(jp);
//		        int id = (Integer) ((IntNode) node.get("id")).numberValue();
//		        String itemName = node.get("itemName").asText();
//		        int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
//		        
//		        return new Item(id, itemName, new User(userId, null));

		return null;
	}

}
