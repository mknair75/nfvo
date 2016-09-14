/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.nfvo.vnfmadapter.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.vnfmadapter.common.VnfmJsonUtil;
import org.openo.nfvo.vnfmadapter.common.VnfmUtil;
import org.openo.nfvo.vnfmadapter.service.constant.Constant;
import org.openo.nfvo.vnfmadapter.service.process.VnfResourceMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;


/**
 * Provide interfaces of resource for VNFM.
 * <br/>
 * 
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
@Path("/vnfmmed/csm/v2/vapps")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VnfResourceRoa {

    private static final Logger LOG = LoggerFactory.getLogger(VnfResourceRoa.class);

    private VnfResourceMgr vnfResourceMgr;

    public void setVnfResourceMgr(VnfResourceMgr vnfResourceMgr) {
        this.vnfResourceMgr = vnfResourceMgr;
    }

    /**
     * Provide function of grant resource.
     * <br/>
     * 
     * @param context
     * @param vnfId
     * @return
     * @since NFVO 0.5
     */
    @PUT
    @Path("/instances/{vnfId}/grant")
    public String grantVnfRes(@Context HttpServletRequest context, @PathParam("vnfId") String vnfId) {
        LOG.info("function=grantVnfRes, msg=enter to grant vnf resource");
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        JSONObject dataObject = VnfmJsonUtil.getJsonFromContexts(context);
        if(null == dataObject) {
            LOG.error("function=grantVnfRes, msg=param error");
            restJson.put("data", "Params error");
            return restJson.toString();
        }

        JSONObject grantObj = dataObject.getJSONObject("grant");

        if(null == grantObj) {
            LOG.error("function=grantVnfRes, msg=param error");
            restJson.put("data", "Grant param error");
            return restJson.toString();
        }

        String reqIp = context.getHeader("x-real-client-addr");
        String vnfmId = VnfmUtil.getVnfmIdByIp(reqIp);

        JSONObject resultObj = vnfResourceMgr.grantVnfResource(grantObj, vnfId, vnfmId);
        handleResult(resultObj, restJson);

        return restJson.toString();
    }

    private void handleResult(JSONObject resultObj, JSONObject restJson) {
        if(resultObj.getInt("retCode") == Constant.REST_SUCCESS) {
            restJson.put("retCode", Constant.REST_SUCCESS);
            restJson.put("data", resultObj.getJSONObject("data"));
        } else {
            if(resultObj.containsKey("data")) {
                String errorMsg = resultObj.getString("data");
                LOG.error("function=handleResult, msg={}", errorMsg);
                restJson.put("data", errorMsg);
            } else {
                LOG.error("function=handleResult, msg=Vnf Resource dispose fail");
                restJson.put("data", "Vnf Resource dispose fail");
            }
        }
    }
}
