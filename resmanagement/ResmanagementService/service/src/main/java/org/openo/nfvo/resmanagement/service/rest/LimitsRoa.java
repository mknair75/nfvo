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

package org.openo.nfvo.resmanagement.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.resmanagement.common.constant.ParamConstant;
import org.openo.nfvo.resmanagement.common.constant.UrlConstant;
import org.openo.nfvo.resmanagement.service.business.inf.LimitsBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Sep 10, 2016
 */
@Path(UrlConstant.LIMITS_URL)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LimitsRoa {

    private static final Logger LOGGER = LoggerFactory.getLogger(LimitsRoa.class);

    private LimitsBusiness limitsBusiness;

    /**
     * get Cpu and Memory Limits.<br>
     *
     * @param context
     * @param tenantId
     * @param vimId
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @GET
    @Path("/{tenantId}/cpu")
    public JSONObject getCpuLimits(@Context HttpServletRequest context,
            @PathParam(ParamConstant.PARAM_TENANTID) String tenantId,
            @QueryParam(ParamConstant.PARAM_VIMID) String vimId) throws ServiceException {
        LOGGER.warn("function=getCpuLimits, tenantId={}, vimId={}", tenantId, vimId);
        JSONObject paramJson = new JSONObject();
        paramJson.put(ParamConstant.PARAM_VIMID, vimId);
        paramJson.put(ParamConstant.PARAM_TENANTID, tenantId);
        return limitsBusiness.getCpuLimits(paramJson);
    }

    /**
     * get Disk Limits.<br>
     *
     * @param context
     * @param tenantId
     * @param vimId
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @GET
    @Path("/{tenantId}/disk")
    public JSONObject getDiskLimits(@Context HttpServletRequest context,
            @PathParam(ParamConstant.PARAM_TENANTID) String tenantId,
            @QueryParam(ParamConstant.PARAM_VIMID) String vimId) throws ServiceException {
        LOGGER.warn("function=getCpuLimits, tenantId={}, vimId={}", tenantId, vimId);
        JSONObject paramJson = new JSONObject();
        paramJson.put(ParamConstant.PARAM_VIMID, vimId);
        paramJson.put(ParamConstant.PARAM_TENANTID, tenantId);
        return limitsBusiness.getDiskLimits(paramJson);
    }

    @GET
    public JSONObject getLimitsResource(@Context HttpServletRequest context,
            @QueryParam(ParamConstant.PARAM_VIMID) String vimId) throws ServiceException {
        LOGGER.warn("function=getLimitsResource, vimId={}", vimId);
        return limitsBusiness.getLimits(vimId);
    }

    public void setLimitsBusiness(LimitsBusiness limitsBusiness) {
        this.limitsBusiness = limitsBusiness;
    }
}
