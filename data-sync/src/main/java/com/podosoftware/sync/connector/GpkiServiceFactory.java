package com.podosoftware.sync.connector;

import org.springframework.beans.factory.FactoryBean;

public class GpkiServiceFactory implements FactoryBean {

	private static final Class objectType = GpkiService.class;
	private String myServerId;
	private String targetServerIdList;
	private String envCertFilePathName;
	private String envPrivateKeyFilePathName;
	private String envPrivateKeyPasswd;
	private String sigCertFilePathName;
	private String sigPrivateKeyFilePathName;
	private String sigPrivateKeyPasswd;
	private String certFilePath;
	private String gpkiLicPath;
	private boolean usingLDAP = true;
	
	
	public GpkiServiceFactory() {
	}

	
	
	public String getMyServerId() {
		return myServerId;
	}



	public void setMyServerId(String myServerId) {
		this.myServerId = myServerId;
	}



	public String getTargetServerIdList() {
		return targetServerIdList;
	}



	public void setTargetServerIdList(String targetServerIdList) {
		this.targetServerIdList = targetServerIdList;
	}



	public String getEnvCertFilePathName() {
		return envCertFilePathName;
	}



	public void setEnvCertFilePathName(String envCertFilePathName) {
		this.envCertFilePathName = envCertFilePathName;
	}



	public String getEnvPrivateKeyFilePathName() {
		return envPrivateKeyFilePathName;
	}



	public void setEnvPrivateKeyFilePathName(String envPrivateKeyFilePathName) {
		this.envPrivateKeyFilePathName = envPrivateKeyFilePathName;
	}



	public String getEnvPrivateKeyPasswd() {
		return envPrivateKeyPasswd;
	}



	public void setEnvPrivateKeyPasswd(String envPrivateKeyPasswd) {
		this.envPrivateKeyPasswd = envPrivateKeyPasswd;
	}



	public String getSigCertFilePathName() {
		return sigCertFilePathName;
	}



	public void setSigCertFilePathName(String sigCertFilePathName) {
		this.sigCertFilePathName = sigCertFilePathName;
	}



	public String getSigPrivateKeyFilePathName() {
		return sigPrivateKeyFilePathName;
	}



	public void setSigPrivateKeyFilePathName(String sigPrivateKeyFilePathName) {
		this.sigPrivateKeyFilePathName = sigPrivateKeyFilePathName;
	}



	public String getSigPrivateKeyPasswd() {
		return sigPrivateKeyPasswd;
	}



	public void setSigPrivateKeyPasswd(String sigPrivateKeyPasswd) {
		this.sigPrivateKeyPasswd = sigPrivateKeyPasswd;
	}



	public String getCertFilePath() {
		return certFilePath;
	}



	public void setCertFilePath(String certFilePath) {
		this.certFilePath = certFilePath;
	}



	public String getGpkiLicPath() {
		return gpkiLicPath;
	}



	public void setGpkiLicPath(String gpkiLicPath) {
		this.gpkiLicPath = gpkiLicPath;
	}



	public boolean isUsingLDAP() {
		return usingLDAP;
	}



	public void setUsingLDAP(boolean usingLDAP) {
		this.usingLDAP = usingLDAP;
	}



	@Override
	public Object getObject() throws Exception {
		
		GpkiService g = new GpkiService();
		
		// 이용기관 서버인증서 경로
		g.setCertFilePath(this.getCertFilePath());
				
		// 이용기관 GPKI API 라이선스파일 경로
		g.setGpkiLicPath(this.getGpkiLicPath());
		g.setEnvCertFilePathName(this.getEnvCertFilePathName());
		g.setEnvPrivateKeyFilePathName(this.getEnvPrivateKeyFilePathName());
		g.setEnvPrivateKeyPasswd(this.getEnvPrivateKeyPasswd());
		// LDAP 의 사용유무
		// 미사용일 경우 암호화할 타겟의 인증서를 파일로 저장해놓고 사용하여야함.
		g.setIsLDAP(this.isUsingLDAP());
		g.setMyServerId(this.getMyServerId());
		g.setSigCertFilePathName(this.getSigCertFilePathName());
		g.setSigPrivateKeyFilePathName(this.getSigPrivateKeyFilePathName());
		g.setSigPrivateKeyPasswd(this.getSigPrivateKeyPasswd());		
		g.setTargetServerIdList(this.getTargetServerIdList());
		
		g.init();
		
		return g;
	}

	public Class getObjectType() {
		return objectType;
	}

	public boolean isSingleton() {
		return false;
	}
}
