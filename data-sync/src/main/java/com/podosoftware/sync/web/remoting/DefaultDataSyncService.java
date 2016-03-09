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

package com.podosoftware.sync.web.remoting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.podosoftware.sync.DataSyncClient;

public class DefaultDataSyncService implements DataSyncService {

    private Log log = LogFactory.getLog(getClass());

    private DataSyncClient dataSyncClient;

    public DataSyncClient getDataSyncClient() {
	return dataSyncClient;
    }

    public void setDataSyncClient(DataSyncClient dataSyncClient) {
	this.dataSyncClient = dataSyncClient;
    }

    public Object process(String jobCode) {
	log.debug("PROCESS " + jobCode);
	return dataSyncClient.process(jobCode);

    }

    public Object read(String jobCode, Object[] args) {
	log.debug("PROCESS " + jobCode);
	return dataSyncClient.read(jobCode, args);
    }

    public void processByNoReturn(String jobCode) {
	log.debug("PROCESS " + jobCode);
	Object obj = dataSyncClient.process(jobCode);
	log.debug("RESULT");
    }

}
