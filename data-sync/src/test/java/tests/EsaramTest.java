package tests;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.podosoftware.sync.DataSyncClient;

public class EsaramTest {
	
	private static Log log = LogFactory.getLog(EsaramTest.class);
	private static ClassPathXmlApplicationContext context = null;
	
	public EsaramTest() {
	}
	
	@BeforeClass
	public static void setup(){
		log.debug("setup context..");
		context = new ClassPathXmlApplicationContext("syncSubsystemContext.xml");
	}
	
	//@Test
	public void testReadFromDb(){
		log.debug("db read test..");
		DataSyncClient client = context.getBean("dataSyncClient", DataSyncClient.class);
	
		
		List<Map<String, Object>> list = client.read("IF_TEST", new String[]{"20130423%"});
		
		log.debug( list.size() + " data readed");
	}
	
	//@Test
	public void testEsaram(){
		log.debug("db read test..");
		DataSyncClient client = context.getBean("dataSyncClient", DataSyncClient.class);
	
		
		Object obj = client.process("IF_001");
		
		log.debug( obj );
	}
		
		

}
