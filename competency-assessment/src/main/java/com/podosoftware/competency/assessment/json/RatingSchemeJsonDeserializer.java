package com.podosoftware.competency.assessment.json;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.podosoftware.competency.assessment.DefaultRatingScheme;
import com.podosoftware.competency.assessment.RatingScheme;

public class RatingSchemeJsonDeserializer extends JsonDeserializer<RatingScheme> {

	private static final Log log = LogFactory.getLog(JsonRatingLevelsDeserializer.class);

	public RatingScheme deserialize(JsonParser jsonParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {		
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);		
		DefaultRatingScheme scheme = new DefaultRatingScheme(node.get("ratingSchemeId").asLong(-1L));
		return scheme;
	}

	
}
