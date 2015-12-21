package com.podosoftware.competency.assessment.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.podosoftware.competency.assessment.DefaultRatingLevel;
import com.podosoftware.competency.assessment.RatingLevel;

public class JsonRatingLevelsDeserializer extends JsonDeserializer<List<RatingLevel>>{

	public JsonRatingLevelsDeserializer() {
	}

	public List<RatingLevel> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		
		//ObjectCodec oc = jsonParser.getCodec();
		//JsonNode node = oc.readTree(jsonParser);
		//oc.readValues(jsonParser, DefaultRatingLevel.class);
		Iterator<DefaultRatingLevel> iter = jsonParser.readValuesAs(DefaultRatingLevel.class);
		List<RatingLevel> list = new ArrayList<RatingLevel>();
		while(iter.hasNext()){
			list.add(iter.next());			
		}
		/**
		Iterator<JsonNode> iter = node.elements();
		while(iter.hasNext()){
			JsonNode child = iter.next();
			
			
			
		}
		**/
		return list;
	}

}
