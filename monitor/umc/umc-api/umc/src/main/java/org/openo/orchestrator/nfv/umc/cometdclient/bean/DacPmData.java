/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
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
package org.openo.orchestrator.nfv.umc.cometdclient.bean;

import lombok.Data;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openo.orchestrator.nfv.umc.util.CustomDateSerializer;

import java.util.Date;
import java.util.Properties;

@Data
public class DacPmData {
    public int taskId;
    @JsonSerialize(using = CustomDateSerializer.class)
    public Date collectTime;
    public int granularity;
    public Properties[] values;
}
