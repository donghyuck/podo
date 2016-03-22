package com.podosoftware.competency.job.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.podosoftware.competency.job.DefaultJob;
import com.podosoftware.competency.job.Job;

public class JobJsonDeserializer extends JsonDeserializer<Job> {

    public JobJsonDeserializer() {
    }

    public Job deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	ObjectCodec oc = jp.getCodec();
	JsonNode node = oc.readTree(jp);
	Job job = new DefaultJob(node.get("jobId").asLong(-1L));
	return job;
    }

}
