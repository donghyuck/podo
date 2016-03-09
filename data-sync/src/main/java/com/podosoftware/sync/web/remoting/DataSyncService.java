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
/**
 * 웹을 통하여 원격으로 DataSyncClient 호출을 위한 서비스 인터페이스 
 * @author donghyuck
 *
 */
public interface DataSyncService {
	
	public Object process(String jobCode) ;
	
	public Object read(String jobCode, Object[] args) ;
	
	public void processByNoReturn(String jobCode);
	
}

