import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Test;

import com.podosoftware.competency.assessment.AssessedEssentialElementScore;

public class StringToFormularTest {

	public StringToFormularTest() {

	}

	
	
	@Test
	public void testStringToFormular(){
		
		AssessedEssentialElementScore sum = new AssessedEssentialElementScore();
		
		ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        Bindings b = engine.getBindings(ScriptContext.ENGINE_SCOPE);

		
	}
}
