/**
 * Copyright 2017 BOCO Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.nfvo.emsdriver.commons.model;

import java.util.HashMap;
import java.util.Map;

public class EMSInfo {
	
	private String name;
	
	private Map<String,CollectVo> collectMap = new HashMap<String,CollectVo>();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public CollectVo getCollectVoByType(String type){
		CollectVo collectVo = null;
		collectVo = this.collectMap.get(type);
		return collectVo;
	}
	
	public void putCollectMap(String type,CollectVo collectVo) {
		
		this.collectMap.put(type, collectVo);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("name = ").append(name).append("\n");
		sb.append("CollectMap = ").append(collectMap);
		
		return sb.toString();
	}
}
