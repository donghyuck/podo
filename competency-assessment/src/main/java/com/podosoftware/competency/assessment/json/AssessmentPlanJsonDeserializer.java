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
import com.podosoftware.competency.assessment.AssessmentPlan;
import com.podosoftware.competency.assessment.DefaultAssessmentPlan;

public class AssessmentPlanJsonDeserializer extends JsonDeserializer<AssessmentPlan> {

    public AssessmentPlanJsonDeserializer() {
    }

    private static final Log log = LogFactory.getLog(JsonRatingLevelsDeserializer.class);

    public AssessmentPlan deserialize(JsonParser jp, DeserializationContext ctxt)
	    throws IOException, JsonProcessingException {
	ObjectCodec oc = jp.getCodec();
	JsonNode node = oc.readTree(jp);
	AssessmentPlan job = new DefaultAssessmentPlan(node.get("assessmentId").asLong(-1L));
	return job;
    }

}
