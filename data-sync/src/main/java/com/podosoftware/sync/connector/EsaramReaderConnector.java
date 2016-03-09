package com.podosoftware.sync.connector;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.podosoftware.sync.DataSyncLogger;
import com.podosoftware.sync.DataSyncLogger.State;
import com.podosoftware.sync.DataSyncLogger.SyncLog;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.ReadConnector;
import architecture.common.jdbc.ParameterMapping;
import architecture.common.util.StringUtils;

public class EsaramReaderConnector implements ReadConnector {

	private static Log log = LogFactory.getLog(EsaramReaderConnector.class);
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
	private EsaramGpkiService gpkiService ;
	private EsaramHttpClient httpClient ;
	private String encoding = EsaramHttpClient.DEFAULT_CHARSET;
	private DataSyncLogger dataSyncLogger ;
	private boolean test = false;
	
	
	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}


	
	public DataSyncLogger getDataSyncLogger() {
		return dataSyncLogger;
	}

	public void setDataSyncLogger(DataSyncLogger dataSyncLogger) {
		this.dataSyncLogger = dataSyncLogger;
	}

	public boolean isSetDataSyncLogger(){
		if(dataSyncLogger!=null) 
			return true;
		
		return false;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public EsaramReaderConnector() {
		
	}
	
	public boolean isSetGpkiService (){
		if( gpkiService != null)
			return true;
		else
			return false;
	}



	public EsaramHttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(EsaramHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public EsaramGpkiService getGpkiService() {
		return gpkiService;
	}

	public void setGpkiService(EsaramGpkiService gpkiService) {
		this.gpkiService = gpkiService;
	}

	protected String getCurrentTimeString(){		
		return formatter.format(new Date());
	}
	
	protected String getReqStartDate(){
		// 어제날짜 구하기
		Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);       
        String yesterday = formatter.format(cal.getTime()).substring(0,8);
        return yesterday;
	}
	
	protected String getReqEndDate(){
		return getCurrentTimeString().substring(0,8);
	}
	
	protected String getTransactionUniqueId(){
		// transactionUniqueId를 조합하기 위한 값 생성
		long startTime = System.currentTimeMillis();		
		String rnd1 = Double.toString(java.lang.Math.random()).substring(2, 6);
		String rnd2 = Double.toString(java.lang.Math.random()).substring(2, 6);
		return getCurrentTimeString() + rnd1 + rnd2;
	}
	
	public Object pull(Context context) {	
		Properties properties = context.getObject("properties", Properties.class);
		List<ParameterMapping> parameterMappings = context.getObject("parameterMappings", List.class);	
		Object[] data = context.getObject("data", Object[].class);	
		if( data == null)
		{
			data = new Object[0];
		}
		
		String reqStartDate ;
		String reqEndDate ;	
		if( data.length == 0 ){
			reqStartDate = getReqStartDate() ;
			reqEndDate = getReqEndDate();
		}else if ( data.length == 2 ){
			reqStartDate 	= (String)data[0];
			reqEndDate		= (String)data[1];
		}else{
			reqStartDate = "";
			reqEndDate = "";
		}

		String serviceUrl           = properties.getProperty("serviceUrl");
		String serviceName 			= properties.getProperty("serviceName");	  // 서비스명:인터페이스정의서 참조(연계목록Sheet-행정공유센터 웹서비스명)
		String useSystemCode 		= properties.getProperty("useSystemCode");	  // 사용자 시스템코드:행정공유센터에 문의(부처변경)
		String certServerId 		= properties.getProperty("certServerId");	  // 부처 인증서 ID(부처변경)
		String transactionUniqueId 	= getTransactionUniqueId();                   // 트랜잭션ID
		String userDeptCode 		= properties.getProperty("userDeptCode");	  // 연계부처코드:7자리(부처변경)
		String userName 			= properties.getProperty("userName");	      // 사용자 이름:연계담당 공무원 성명(부처변경)		
		String xmlnsUrl             = properties.getProperty("xmlns_url");
		
		boolean hasError = false;
		SyncLog syncLog = new SyncLog(transactionUniqueId, serviceName);
		
		/**************************************************
		 * 요청 XML 생성
		 **************************************************/
		StringBuffer sb = new StringBuffer();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"").append(xmlnsUrl).append("\">").append("/n");
		sb.append("   <soapenv:Header>").append("/n");
		sb.append("      <typ:commonHeader>").append("/n");
		sb.append("         <typ:serviceName>").append(serviceName).append("</typ:serviceName>").append("/n");
		sb.append("         <typ:useSystemCode>").append(useSystemCode).append("</typ:useSystemCode>").append("/n");
		sb.append("         <typ:certServerId>").append(certServerId).append("</typ:certServerId>").append("/n");
		sb.append("         <typ:transactionUniqueId>").append(transactionUniqueId).append("</typ:transactionUniqueId>").append("/n");
		sb.append("         <typ:userDeptCode>").append(userDeptCode).append("</typ:userDeptCode>").append("/n");
		sb.append("         <typ:userName>").append(userName).append("</typ:userName>").append("/n");
		sb.append("      </typ:commonHeader>").append("/n");
		sb.append("   </soapenv:Header>").append("/n");
		sb.append("   <soapenv:Body>").append("/n");
		// TODO 서비스 명이 바뀌면 수정해야함. (요청은 "<typ:get" + 서비스명 + ">" 임.
		sb.append("      <typ:get").append(serviceName).append(">").append("/n");
		sb.append("         <typ:reqStartDate>").append(reqStartDate).append("</typ:reqStartDate>").append("/n");
		sb.append("         <typ:reqEndDate>").append(reqEndDate).append("</typ:reqEndDate>").append("/n");
		// TODO 서비스 명이 바뀌면 수정해야함. (요청은 "</typ:get" + 서비스명 + ">" 임.
		sb.append("      </typ:get").append(serviceName).append(">").append("/n");
		sb.append("   </soapenv:Body>").append("/n");
		sb.append("</soapenv:Envelope>").append("/n");
		String requestXml = sb.toString();
		
		String responseXml = "";
		
		log.info("CREATE XML REQUEST :" + requestXml );
		
		try {
			/**************************************************
			 * 송신데이터 암호화
			 **************************************************/
			// TODO 서비스 명이 바뀌면 수정해야함. (요청은 "<typ:get" + 서비스명 + ">" 임.
			String original = requestXml
				.split("<typ:get"+serviceName+">")[1]
			    .split("</typ:get"+serviceName+">")[0];
			
			if( isSetGpkiService() ){
				//암호화
				byte[] encrypted 	= gpkiService.encrypt(original.getBytes(this.encoding), gpkiService.getTargetServerIdList());
				//전자서명
				byte[] signed 		= gpkiService.sign(encrypted);
				
				//base64인코딩
				String encoded = gpkiService.encode(signed);
				
				log.debug("original:" + original);
				log.debug("encoded:" + encoded );
				
				requestXml = requestXml.replaceAll(original, encoded);
			}
			
		} catch (Exception e) {
			log.error(e);	
			hasError = true;
			syncLog.setState(State.FAIL);
			syncLog.setErrorMsg("전송데이터 암호화 실패");
			syncLog.setError(e);
			if(isSetDataSyncLogger()){
				this.dataSyncLogger.write(syncLog);
			}			
			return Collections.emptyList();
		}		
		
		try {
			
			String responseStr = httpClient.send(serviceUrl, requestXml);			
			// TODO 서비스 명이 바뀌면 수정해야함. (응답은 "<typ:get" + 서비스명 에서 "Service"가 빠진것 + "Response>" 임.
			log.debug("checking response date !!");
			String resEncodeStr = responseStr
					.split("<typ:get" + StringUtils.removeEnd(serviceName, "Service") + "Response xmlns:typ=\"" + xmlnsUrl +  "\">")[1]
					.split("</typ:get" + StringUtils.removeEnd(serviceName, "Service") + "Response>")[0];
			
			if (responseStr.indexOf("Fault>") > 0) {
					/**************************************************
					 * 응답 데이터 복호화
					 **************************************************/
				if( isSetGpkiService() ){
					byte[] decoded;
					try {
					    //base64디코딩
						decoded = gpkiService.decode(resEncodeStr);
						 //전자서명확인
						byte[] validated = gpkiService.validate(decoded);
						 //복호화
						
						log.debug( "ERROR XML RESPONSE : " +
							new String(gpkiService.decrypt(validated), httpClient.getCharset())
						);
						throw new EsaramException("RESPONSE IS FAULT!!!" + new String(gpkiService.decrypt(validated), httpClient.getCharset()));		
						
					} catch (Exception e) {
						//"응답 오류 데이터 복호화 실패"
						throw new EsaramException("응답 데이터 복호화 실패", e);
					}
				}
				throw new EsaramException("RESPONSE IS FAULT!!!");				
			}else if(responseStr.trim().equals("") || responseStr == null){
				throw new EsaramException("RESPONSE IS EMPTY!!!");
			}
			
			String decrypted = "";	
			if( isSetGpkiService() ){
					
				/**************************************************
				 * 응답 데이터 복호화
				 **************************************************/
				byte[] decoded;
				try {
				    //base64디코딩
					decoded = gpkiService.decode(resEncodeStr);
					 //전자서명확인
					byte[] validated = gpkiService.validate(decoded);
					 //복호화
					decrypted = new String(gpkiService.decrypt(validated), httpClient.getCharset());
				} catch (Exception e) {
					throw new EsaramException("수신데이터 복호화 오류", e);
				}
			}else{
				decrypted = resEncodeStr;
			}	
			
			log.info("XML RESPONSE :" + decrypted );
			responseXml = "<data>" +  StringUtils.remove(decrypted, "typ:") + "</data>";
		} catch (Throwable e) {		
			hasError = true;
			syncLog.setState(State.FAIL);
			syncLog.setError(e);
			if( e instanceof IOException ){
				syncLog.setErrorMsg("데이터 통신 오류 : " + e.getMessage() );
			}else if ( e instanceof EsaramException ){
				syncLog.setErrorMsg("e사람 오류 : " + e.getMessage());				
			}
			else{
				syncLog.setErrorMsg("수신테이터 처리 오류");
			
			}
			log.error(e);			
		}	
		
		List<Map<String, Object>>  list = xmlToList(responseXml, syncLog.getUid());
		if(isSetDataSyncLogger()){
			if( !hasError && StringUtils.isEmpty(syncLog.getErrorMsg()))
				syncLog.setState(State.SUCCESS);
			
			syncLog.setRowCount(list.size());
			this.dataSyncLogger.write(syncLog);
		}
		return list ;
	}
	
	
	protected List<Map<String, Object>> xmlToList (String xml , String uid) {	
		
		if( StringUtils.isEmpty(xml))
			return Collections.emptyList();		
		SAXReader saxReader = new SAXReader();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			Document doc = saxReader.read(new StringReader(xml));
			Element root = doc.getRootElement();
			List<Element> list = root.elements("dataList");			
			for(Element ele : list){
				Map<String, Object> row = new HashMap<String, Object>();
				List<Element> attrs = ele.elements();
				for( Element attr : attrs ){
					String name = attr.getName();
					String value = attr.getTextTrim();
					row.put(name, value);		
				}
				if(StringUtils.isNotEmpty(uid))
					row.put("SYNC_ID", uid);
				result.add(row);
			}
				
		} catch (Exception e) {
			log.error(e);
			return Collections.emptyList();
		}
		return result;
	}

}
