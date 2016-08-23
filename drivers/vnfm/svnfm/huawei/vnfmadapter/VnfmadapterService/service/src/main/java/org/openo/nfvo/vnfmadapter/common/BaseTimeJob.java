/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.nfvo.vnfmadapter.common;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseTimeJob implements Runnable {

    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    private long initialDelay = 1;

    private long period = 1;

    private String startTime = "";

    @Override
    public abstract void run();

    public void stop() {
        service.shutdown();
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void start() {
        if(startTime.length() != 0) {
            String[] VNFtime = startTime.split(":");
            if(VNFtime.length == 2) {
                int minute = Integer.parseInt(VNFtime[0]) * 60 + Integer.parseInt(VNFtime[1]);
                Calendar calendar = Calendar.getInstance();
                int curMinute = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
                if(curMinute <= minute) {
                    initialDelay = (minute - curMinute) * 60L;
                } else {
                    initialDelay = (minute + 24 * 60 - curMinute) * 60L;
                }
            }
        }
        service.scheduleAtFixedRate(this, initialDelay, period, TimeUnit.SECONDS);
    }
}