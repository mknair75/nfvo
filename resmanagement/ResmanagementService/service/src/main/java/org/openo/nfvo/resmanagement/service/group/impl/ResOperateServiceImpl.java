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

package org.openo.nfvo.resmanagement.service.group.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.nfvo.resmanagement.common.ResourceUtil;
import org.openo.nfvo.resmanagement.common.constant.ParamConstant;
import org.openo.nfvo.resmanagement.common.util.JsonUtil;
import org.openo.nfvo.resmanagement.common.util.StringUtil;
import org.openo.nfvo.resmanagement.service.base.fs.inf.Host;
import org.openo.nfvo.resmanagement.service.base.fs.inf.InterfaceResManagement;
import org.openo.nfvo.resmanagement.service.base.fs.inf.Network;
import org.openo.nfvo.resmanagement.service.base.fs.inf.Port;
import org.openo.nfvo.resmanagement.service.base.fs.inf.Sites;
import org.openo.nfvo.resmanagement.service.base.fs.inf.Vim;
import org.openo.nfvo.resmanagement.service.entity.VimEntity;
import org.openo.nfvo.resmanagement.service.group.inf.ResOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONObject;

/**
 * 
 * Resource operation service implementation class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version     NFVO 0.5  Sep 10, 2016
 */
public class ResOperateServiceImpl implements ResOperateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResOperateServiceImpl.class);

    private Sites sites;

    private Network network;

    private Host host;

    private Port port;

    private Vim vim;

    private IResourceAddServiceImpl iResourceAddServiceImpl;

    private IResourceUpdateServiceImpl iResourceUpdateServiceImpl;

    private IResourceDelServiceImpl iResourceDelServiceImpl;

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void addIRes(String tenantId, String vimId, JSONObject header) throws ServiceException {
        LOGGER.warn("function=addIRes; msg=add IResource by vimId:[{}], tenantId:[{}]", vimId, tenantId);
        if(!StringUtil.isValidString(vimId) || !StringUtil.isValidString(tenantId)) {
            LOGGER.warn("function=addIRes; msg=vimId[{}] is valid", vimId);
            throw new ServiceException(
                    ResourceUtil.getMessage("org.openo.nfvo.resmanage.service.group.resoperate.add.res.no.vimId"));
        }

        RestfulParametes restParametes = createRestfulParametes(tenantId, vimId, header);
        HashMap<String, InterfaceResManagement> iResMap = createMap();

        if(vim.add(vimId) <= 0) {
            LOGGER.error("add vimId to vim table fail");
        }

        iResourceAddServiceImpl.addIRes(restParametes, iResMap);
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void updateIRes(String tenantId, String vimId, JSONObject header) throws ServiceException {
        if(!StringUtil.isValidString(vimId)) {
            LOGGER.error("function=updateIRes; msg=vimId is not exist");
            throw new ServiceException(
                    ResourceUtil.getMessage("org.openo.nfvo.resmanage.service.group.resoperate.update.res.no.vimId"));
        }

        RestfulParametes restParametes = createRestfulParametes(tenantId, vimId, header);
        HashMap<String, InterfaceResManagement> iResMap = createMap();
        iResourceUpdateServiceImpl.updateIRes(vimId, restParametes, iResMap);
    }

    @Override
    public void updateAllIRes() throws ServiceException {
        LOGGER.warn("function=updateAllIRes; msg=update all IResource");
        List<VimEntity> vims = vim.getList();
        if(vims.isEmpty()) {
            LOGGER.error("function=updateAllIRes; msg=vimId is not exist");
            throw new ServiceException(
                    ResourceUtil.getMessage("org.openo.nfvo.resmanage.service.group.resoperate.update.res.no.vimId"));
        }
        for(VimEntity vimEntity : vims) {
            String vimId = vimEntity.getId();
            LOGGER.warn("function=updateAllIRes; msg=start update vimId:{}", vimId);
            updateIRes(null, vimId, new JSONObject());
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public int deleteIRes(String vimId) throws ServiceException {
        LOGGER.warn("function=deleteAllRes; msg=deleteResPool vimId: {}", vimId);
        if(StringUtils.isEmpty(vimId)) {
            LOGGER.error("function=deleteAllRes; msg=vimId is null");
            throw new ServiceException(
                    ResourceUtil.getMessage("org.openo.nfvo.resmanage.service.group.resoperate.res.no.vimId"));
        }

        return iResourceDelServiceImpl.deleteIRes(vimId, createMap(), vim);
    }

    private RestfulParametes createRestfulParametes(String tenantId, String vimId, JSONObject header) {
        RestfulParametes restParametes = new RestfulParametes();
        restParametes.put("vimId", vimId);
        restParametes.put("tenantId", tenantId);
        restParametes.putHttpContextHeader("Content-Type", "application/json");
        JSONObject headers = JsonUtil.getJsonFieldJson(header, "header");
        if(null == headers || !headers.has("x-auth-token")) {
            String token = "TestToken";
            LOGGER.warn("function=createRestfulParametes; msg=create token.");
            restParametes.putHttpContextHeader("X-Auth-Token", token);
            return restParametes;
        }
        restParametes.putHttpContextHeader("X-Auth-Token", JsonUtil.getJsonFieldStr(headers, "X-Auth-Token"));
        return restParametes;
    }

    private HashMap<String, InterfaceResManagement> createMap() {
        HashMap<String, InterfaceResManagement> iResMap = new HashMap<String, InterfaceResManagement>(10);
        iResMap.put(ParamConstant.PARAM_NETWORK, network);
        iResMap.put(ParamConstant.PARAM_HOST, host);
        iResMap.put(ParamConstant.PARAM_PORT, port);
        return iResMap;
    }

    public void setSites(Sites sites) {
        this.sites = sites;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public void setVim(Vim vim) {
        this.vim = vim;
    }

    /**
     * 
     * Set iResource Add service implemtation.<br>
     * 
     * @param iResourceAddServiceImpl
     * @since  NFVO 0.5
     */
    public void setiResourceAddServiceImpl(IResourceAddServiceImpl iResourceAddServiceImpl) {
        this.iResourceAddServiceImpl = iResourceAddServiceImpl;
    }

    /**
     * 
     * Set iResource update service implementation.<br>
     * 
     * @param iResourceUpdateServiceImpl
     * @since  NFVO 0.5
     */
    public void setiResourceUpdateServiceImpl(IResourceUpdateServiceImpl iResourceUpdateServiceImpl) {
        this.iResourceUpdateServiceImpl = iResourceUpdateServiceImpl;
    }

    /**
     * 
     * Set iresource delete service implementation.<br>
     * 
     * @param iResourceDelServiceImpl
     * @since  NFVO 0.5
     */
    public void setiResourceDelServiceImpl(IResourceDelServiceImpl iResourceDelServiceImpl) {
        this.iResourceDelServiceImpl = iResourceDelServiceImpl;
    }
}
