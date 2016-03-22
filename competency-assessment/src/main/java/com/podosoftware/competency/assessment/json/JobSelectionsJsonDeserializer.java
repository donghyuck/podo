package com.podosoftware.competency.assessment.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.podosoftware.competency.assessment.JobSelection;

public class JobSelectionsJsonDeserializer extends JsonDeserializer<List<JobSelection>> {

    @Override
    public List<JobSelection> deserialize(JsonParser jsonParser, DeserializationContext ctxt)
	    throws IOException, JsonProcessingException {

	List<JobSelection> list = new ArrayList<JobSelection>();
	ObjectCodec oc = jsonParser.getCodec();
	JsonNode node = oc.readTree(jsonParser);
	Iterator<JsonNode> iter = node.elements();
	while (iter.hasNext()) {
	    JsonNode child = iter.next();
	    list.add(new JobSelection(child.get("selectionId").asLong(-1L), child.get("objectType").asInt(0),
		    child.get("objectId").asLong(-1L), child.get("classifyType").asLong(-1L),
		    child.get("classifiedMajorityId").asLong(-1L), child.get("classifiedMiddleId").asLong(-1L),
		    child.get("classifiedMinorityId").asLong(-1L), child.get("jobId").asLong(-1L)));
	}
	return list;
    }

}
