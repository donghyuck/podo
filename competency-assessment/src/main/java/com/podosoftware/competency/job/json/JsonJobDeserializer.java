package com.podosoftware.competency.job.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.podosoftware.competency.job.DefaultJob;
import com.podosoftware.competency.job.Job;

public class JsonJobDeserializer extends JsonDeserializer<Job> {

	public JsonJobDeserializer() {
	}

	public Job deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		
		return oc.readValue(jp, DefaultJob.class);
	}

}
