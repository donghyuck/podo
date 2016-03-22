package com.podosoftware.competency.assessment.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.podosoftware.competency.assessment.DefaultRatingLevel;
import com.podosoftware.competency.assessment.RatingLevel;

public class JsonRatingLevelsDeserializer extends JsonDeserializer<List<RatingLevel>> {

    private static final Log log = LogFactory.getLog(JsonRatingLevelsDeserializer.class);

    public JsonRatingLevelsDeserializer() {
    }

    public List<RatingLevel> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
	    throws IOException, JsonProcessingException {
	List<RatingLevel> list = new ArrayList<RatingLevel>();
	ObjectCodec oc = jsonParser.getCodec();
	JsonNode node = oc.readTree(jsonParser);
	Iterator<JsonNode> iter = node.elements();
	while (iter.hasNext()) {
	    JsonNode child = iter.next();
	    list.add(new DefaultRatingLevel(child.get("ratingSchemeId").asLong(-1L),
		    child.get("ratingLevelId").asLong(-1L), child.get("title").asText(), child.get("score").asInt(0)));
	}
	/**
	 * log.debug(list);
	 * 
	 * Iterator<DefaultRatingLevel> iter2 = oc.readValues(jsonParser,
	 * DefaultRatingLevel.class);;
	 * //jsonParser.readValuesAs(DefaultRatingLevel.class);
	 * 
	 * log.debug(iter2); while(iter.hasNext()){
	 * log.debug(iter.next().toString()); }
	 */
	return list;
    }

}
