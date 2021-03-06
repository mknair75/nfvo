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
package org.openo.nfvo.monitor.umc.drill.wrapper.handler.drill;

import org.openo.nfvo.monitor.umc.drill.resources.bean.response.NodeInformation;
import org.openo.nfvo.monitor.umc.drill.wrapper.common.exception.TopologyException;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.AbstractTopologyHandler;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.ResourceServicesStub;

/**
 *       The concrete handler that process the drill request of the HOST
 */
public class HostDrillHandler extends AbstractTopologyHandler {

    /**
     * query the Host info
     */
    @Override
    public NodeInformation queryCurrentNode(String resourceid) throws TopologyException {
    	return ResourceServicesStub.qureyHost(resourceid);
    }

    /**
     * query all the VDU nodes deployed on the host
     */
    @Override
    public NodeInformation[] queryParentNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
    	return ResourceServicesStub.queryVdusOfHost(currentNodeDetail);
    }

    /**
     * Host node is the leaf,dose not have child,return null directly
     */
    @Override
    public NodeInformation[] queryChildNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        return null;
    }

}
