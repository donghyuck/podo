package com.podosoftware.competency.codeset.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomJsonLongSerializer extends JsonSerializer<Long> {

    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
	    throws IOException, JsonProcessingException {
	if (value == null || value == -1L) {
	    jgen.writeNull();
	} else {
	    jgen.writeNumber(value);
	}
    }

}
