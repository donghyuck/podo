package com.podosoftware.competency.codeset.json;

import java.io.IOException;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomJsonLongDeserializer extends JsonDeserializer<Long> {

	@Override
	public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {		
		if(StringUtils.isEmpty(jp.getText()) ){
			return -1L ;
		}
		return jp.getLongValue();
	}

}
