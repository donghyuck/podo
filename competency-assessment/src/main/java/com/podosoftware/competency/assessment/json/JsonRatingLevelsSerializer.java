package com.podosoftware.competency.assessment.json;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.podosoftware.competency.assessment.RatingLevel;

public class JsonRatingLevelsSerializer extends JsonSerializer<List<RatingLevel>>{

	public JsonRatingLevelsSerializer() {
	}

	public void serialize(List<RatingLevel> value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeObject(value);
	}

}
