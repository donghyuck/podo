package com.podosoftware.competency.job.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.DefaultClassification;

public class CustomJsonClassificationDeserializer extends JsonDeserializer<Classification> {

    public CustomJsonClassificationDeserializer() {
    }

    public Classification deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
	    throws IOException, JsonProcessingException {
	ObjectCodec oc = jsonParser.getCodec();
	JsonNode node = oc.readTree(jsonParser);
	Classification classify = new DefaultClassification();
	classify.setClassifyType(node.get("classifyType").longValue());
	classify.setClassifiedMajorityId(node.get("classifiedMajorityId").longValue());
	classify.setClassifiedMiddleId(node.get("classifiedMiddleId").longValue());
	classify.setClassifiedMinorityId(node.get("classifiedMinorityId").longValue());
	return classify;
    }

}
