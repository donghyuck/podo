/*
 * Copyright 2016 donghyuck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.podosoftware.sync.scheduling;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.podosoftware.sync.DataSyncClient;

/**
 * 
 * DataSyncClient 를 quartz 를 통하여 호출할수 있도록 한다.
 * 호출시 pipelineMappings 에 등록된 키로만 호출이 가능하다.
 * 
 * @author donghyuck
 *
 */
public class DataSyncJob extends QuartzJobBean {

    private Log log = LogFactory.getLog(DataSyncJob.class);

    private String jobCode;

    private DataSyncClient dataSyncClient;

    public String getJobCode() {
	return jobCode;
    }

    public void setDataSyncClient(DataSyncClient dataSyncClient) {
	this.dataSyncClient = dataSyncClient;
    }

    public void setJobCode(String jobCode) {
	this.jobCode = jobCode;
    }

    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
	long start = System.currentTimeMillis();
	try {	    
	    log.info("execute sync job [" + jobCode + "] by " + dataSyncClient.getClass().getName());
	    //System.out.println("execute sync job : " + jobCode + " with " + dataSyncClient.getClass().getName());
	    dataSyncClient.process(jobCode);
	    
	} catch (Throwable e) {
	    log.error(e);
	    throw new JobExecutionException(e);
	} finally {
	    long end = System.currentTimeMillis();
	    log.info("sync job [" + jobCode + "] done in " + (end - start) + "ms.");	    
	}
    }

}
