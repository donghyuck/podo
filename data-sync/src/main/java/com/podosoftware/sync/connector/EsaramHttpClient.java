package com.podosoftware.sync.connector;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EsaramHttpClient  {

	public static final String DEFAULT_CHARSET = "UTF-8" ;
	
	public static final String DEFAULT_CONTENT_TYPE = "text/xml; charset=utf-8" ;
	
	/**
	 * 문자 인코딩 형식
	 */
	private String charset = DEFAULT_CHARSET ;
	
	/**
	 * 메시지 형식
	 */
	private String contentType = DEFAULT_CONTENT_TYPE ;
	
	/**
	 * 연결 타임아웃(단위 : 밀리세컨드) 30초
	 */
	private int  connectTimeout = 5 * 60 * 1000; ;
	
	/**
	 * 응답 타임아웃(단위 : 밀리세컨드) 3분
	 */
	private int soTimeout = 900 * 1000;
	
	/**
	 * 재 접속 최대 횟수
	 */
	private int retryMaxCount = 1;
	
	private static Log log = LogFactory.getLog(EsaramHttpClient.class);
	
	public EsaramHttpClient() {
	}


	public String getCharset() {
		return charset;
	}


	public void setCharset(String charset) {
		this.charset = charset;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public int getConnectTimeout() {
		return connectTimeout;
	}


	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}


	public int getSoTimeout() {
		return soTimeout;
	}


	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}


	public int getRetryMaxCount() {
		return retryMaxCount;
	}


	public void setRetryMaxCount(int retryMaxCount) {
		this.retryMaxCount = retryMaxCount;
	}


	public String send(String serviceUrl, String requestXml) throws Exception {
		
		PostMethod method = null; // XML 송신 메소드 선언

		String responseXml = null; // XML 수신
		try {

			HttpClient client; // XML 송신 클라이언트 생성
			{
				HttpConnectionManagerParams params = new HttpConnectionManagerParams();
				params.setConnectionTimeout(getConnectTimeout());
				params.setSoTimeout(getSoTimeout());
				params.setTcpNoDelay(true);
				HttpConnectionManager conn = new SimpleHttpConnectionManager();
				conn.setParams(params);
				client = new HttpClient(conn);

				method = new PostMethod(serviceUrl);
				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
				method.setRequestHeader("Content-Type", contentType);
				method.setRequestHeader("Connection", "close");
				RequestEntity requestEntity = new StringRequestEntity( requestXml, contentType, charset);
				method.setRequestEntity(requestEntity);
			}

			int tryCount = 0;
			while(true){
				
				tryCount++;
				
				log.debug("["+ tryCount +"] try connection --------------------");
				
				int responseCode = client.executeMethod(method); // XML 송신 및 응답코드 수신				
				log.debug("responseCode=" + responseCode);
				{
					InputStream is = method.getResponseBodyAsStream();
					try {
						int readLen;
						byte[] buffer = new byte[1024];
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						while ((readLen = is.read(buffer)) >= 0) {
							baos.write(buffer, 0, readLen);
						}
						byte[] data = baos.toByteArray();
						responseXml = new String(data, charset);
					} finally {
						IOUtils.closeQuietly(is);
					}
				}

//					System.out.println("responseXml=\n" + responseXml);

				// 결과 처리
				if (responseCode == HttpStatus.SC_OK) {
					// 정상 응답인 경우
					log.debug("HTTP CALL SUCCESS. STATUS : " + responseCode);
					break;
					
				} else if (responseXml.indexOf("Fault>") > 0) {
					// 서비스 오류가 발생한 경우
					log.debug("Response is Fault Message.");
				} else {
					// 통신 오류가 발생한 경우
					log.debug("HTTP ERROR. STATUS : " + responseCode);
				}
				
				if(tryCount == retryMaxCount){
					log.debug("Try Count is over! break. RetryMaxCount : " + retryMaxCount + ". TryCount : " + tryCount);
					break;
				}else{
					log.debug("30sec sleep and retry. TryCount : " + tryCount);
					Thread.sleep(30 * 1000L);
				}

			}// end for 재처리
		
		} finally {
			if (method != null) {
				method.releaseConnection();// 메소스 종료
			}
		}		
		return responseXml;
	}
	
}
