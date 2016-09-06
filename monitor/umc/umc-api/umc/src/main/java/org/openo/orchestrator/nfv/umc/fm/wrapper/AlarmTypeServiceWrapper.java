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
package org.openo.orchestrator.nfv.umc.fm.wrapper;

import java.util.List;

import org.openo.orchestrator.nfv.umc.fm.cache.FmCacheProcess;
import org.openo.orchestrator.nfv.umc.fm.db.entity.AlarmType;
import org.openo.orchestrator.nfv.umc.fm.db.process.FmDBProcess;
import org.openo.orchestrator.nfv.umc.fm.resource.bean.request.AlarmTypeQueryRequest;

public class AlarmTypeServiceWrapper {

    public AlarmType getAlarmType(int typeid, String language) {
        AlarmType alarmType = FmCacheProcess.queryAlarmType(typeid);
        alarmType.translate(language);
        return alarmType;
    }

    public AlarmType[] getAlarmTypes(AlarmTypeQueryRequest req, String language) {
        List<AlarmType> alarmType = FmCacheProcess.queryAlarmTypes(req.getTypeid());
        for(int i=0;i<alarmType.size();i++){
            alarmType.get(i).translate(language);
        }
        return alarmType.toArray(new AlarmType[alarmType.size()]);
    }

}
