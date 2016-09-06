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
package org.openo.orchestrator.nfv.dac.dataaq.datacollector.cli;

import java.io.IOException;

public interface ClientSession {
    void connect(String s, int i) throws IOException;

    void connect(String s) throws IOException;

    void disconnect() throws IOException;

    void login(String s, String s1) throws IOException;

    String send(String s) throws IOException;
}
