# Copyright 2016 ZTE Corporation.
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#         http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import json
import logging
import inspect
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from driver.pub.utils import restcall
from driver.pub.utils.restcall import req_by_msb
logger = logging.getLogger(__name__)


def fun_name():
    return "=================%s==================" % inspect.stack()[1][3]


def ignorcase_get(args, key):
    if not key:
        return ""
    if not args:
        return ""
    if key in args:
        return args[key]
    for old_key in args:
        if old_key.upper() == key.upper():
            return args[old_key]
    return ""


def mapping_conv(keyword_map, rest_return):
    resp_data = {}
    for param in keyword_map:
        if keyword_map[param]:
            resp_data[keyword_map[param]] = ignorcase_get(rest_return, param)
    return resp_data

query_vnfd_url = "openoapi/nslcm/v1/vnfpackage/%s"
query_vnfm_url = "openoapi/extsys/v1/vnfms/%s"
query_package_url = "openoapi/nslcm/v1/vnfpackage/%s"


# Query VNFM by VNFMID
def vnfm_get(vnfmid):
    ret = req_by_msb("openoapi/extsys/v1/vnfms/%s" % vnfmid, "GET")
    return ret


def vnfd_get(vnfpackageid):
    ret = req_by_msb("openoapi/nslcm/v1/vnfpackage/%s" % vnfpackageid, "POST")
    return ret


def vnfpackage_get(csarid):
    ret = req_by_msb("openoapi/nslcm/v1/vnfpackage/%s" % csarid, "GET")
    return ret


# ==================================================
create_vnf_url = "v1/vnfs"
create_vnf_param_mapping = {
    "packageUrl": "",
    "instantiateUrl": "",
    "instantiationLevel": "",
    "vnfInstanceName": "",
    "vnfPackageId": "",
    "vnfDescriptorId": "",
    "flavorId": "",
    "vnfInstanceDescription": "",
    "extVirtualLink": "",
    "additionalParam": ""
}
create_vnf_resp_mapping = {
    "VNFInstanceID": "vnfInstanceId",
    "JobId": "jobid",
}


@api_view(http_method_names=['POST'])
def instantiate_vnf(request, *args, **kwargs):
    try:
        logger.debug("[%s] request.data=%s", fun_name(),request.data)
        vnfm_id = ignorcase_get(kwargs, "vnfmid")
        ret = vnfm_get(vnfm_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        vnfm_info = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s] vnfm_info=%s", fun_name(), vnfm_info)
        vnf_package_id = ignorcase_get(request.data, "vnfPackageId")
        ret = vnfd_get(vnf_package_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        vnfd_info = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s] vnfd_info=%s", fun_name(), vnfd_info)
        csar_id = ignorcase_get(vnfd_info, "csarId")
        ret = vnfpackage_get(csar_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        vnf_package_info = json.JSONDecoder().decode(ret[1])
        packageInfo = ignorcase_get(vnf_package_info, "packageInfo")
        logger.debug("[%s] packageInfo=%s", fun_name(), packageInfo)
        data = {}
        data["NFVOID"] = 1
        data["VNFMID"] = vnfm_id
        data["VNFD"] = ignorcase_get(packageInfo, "downloadUri")
        data["VNFURL"] = ignorcase_get(packageInfo, "downloadUri")
        # data["extension"] = ignorcase_get(request.data, "additionalparam")
        # data["extension"]["vnfinstancename"] = ignorcase_get(request.data, "vnfinstancename")
        # data["VNFID"] =
        logger.debug("[%s] call_req data=%s", fun_name(), data)
        ret = restcall.call_req(
            base_url=ignorcase_get(vnfm_info, "url"),
            user=ignorcase_get(vnfm_info, "userName"),
            passwd=ignorcase_get(vnfm_info, "password"),
            auth_type=restcall.rest_no_auth,
            resource=create_vnf_url,
            method='post',
            content=json.JSONEncoder().encode(data))
        logger.debug("[%s] call_req ret=%s", fun_name(), ret)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        resp = json.JSONDecoder().decode(ret[1])
        resp_data = mapping_conv(create_vnf_resp_mapping, resp)
        logger.info("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred when instantiating VNF")
        raise e
    return Response(data=resp_data, status=status.HTTP_202_ACCEPTED)


# ==================================================
vnf_delete_url = "v1/vnfs/%s"
vnf_delete_param_mapping = {
    "terminationType": "terminationType",
    "gracefulTerminationTimeout": "gracefulTerminationTimeout"
}
vnf_delete_resp_mapping = {
    "vnfInstanceId": "vnfInstanceId",
    "JobId": "jobid"
}


@api_view(http_method_names=['POST'])
def terminate_vnf(request, *args, **kwargs):
    try:
        logger.debug("[%s] request.data=%s", fun_name(), request.data)
        vnfm_id = ignorcase_get(kwargs, "vnfmid")
        ret = vnfm_get(vnfm_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        vnfm_info = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s] vnfm_info=%s", fun_name(), vnfm_info)
        data = {}
        logger.debug("[%s]req_data=%s", fun_name(), data)
        ret = restcall.call_req(
            base_url=ignorcase_get(vnfm_info, "url"),
            user=ignorcase_get(vnfm_info, "userName"),
            passwd=ignorcase_get(vnfm_info, "password"),
            auth_type=restcall.rest_no_auth,
            resource=vnf_delete_url % (ignorcase_get(kwargs, "vnfInstanceID")),
            method='post',
            content=json.JSONEncoder().encode(data))
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        resp = json.JSONDecoder().decode(ret[1])
        resp_data = mapping_conv(vnf_delete_resp_mapping, resp)
        logger.debug("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred when terminating VNF")
        raise e
    return Response(data=resp_data, status=status.HTTP_202_ACCEPTED)


# ==================================================


vnf_detail_url = "v1/vnfs/%s"
vnf_detail_resp_mapping = {
    "VNFInstanseStatus": "status",
}


@api_view(http_method_names=['GET'])
def query_vnf(request, *args, **kwargs):
    try:
        logger.debug("[%s] request.data=%s", fun_name(), request.data)
        vnfm_id = ignorcase_get(kwargs, "vnfmid")
        ret = vnfm_get(vnfm_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        vnfm_info = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s] vnfm_info=%s", fun_name(), vnfm_info)
        data = {}
        ret = restcall.call_req(
            base_url=ignorcase_get(vnfm_info, "url"),
            user=ignorcase_get(vnfm_info, "userName"),
            passwd=ignorcase_get(vnfm_info, "password"),
            auth_type=restcall.rest_no_auth,
            resource=vnf_detail_url % (ignorcase_get(kwargs, "vnfInstanceID")),
            method='get',
            content=json.JSONEncoder().encode(data))
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        resp = json.JSONDecoder().decode(ret[1])
        vnf_status = ignorcase_get(resp, "vnfinstancestatus")
        resp_data = {"vnfInfo": {"vnfStatus": vnf_status}}
        logger.debug("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred when querying VNF information.")
        raise e
    return Response(data=resp_data, status=status.HTTP_202_ACCEPTED)


# Get Operation Status
operation_status_url = '/v1/jobs/{jobId}?NFVOID={nfvoId}&VNFMID={vnfmId}&ResponseID={responseId}'
operation_status_resp_map = {
    "JobId": "jobId",
    "Status": "status",
    "Progress": "progress",
    "StatusDescription": "currentStep",
    "ErrorCode": "errorCode",
    "ResponseId": "responseId",
    "ResponseHistoryList": "responseHistoryList",
    "ResponseDescriptor": "responseDescriptor",
}


@api_view(http_method_names=['GET'])
def operation_status(request, *args, **kwargs):
    data = {}
    try:
        logger.debug("[%s] request.data=%s", fun_name(), request.data)
        vnfm_id = ignorcase_get(kwargs, "vnfmid")
        ret = vnfm_get(vnfm_id)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        vnfm_info = json.JSONDecoder().decode(ret[1])
        logger.debug("[%s] vnfm_info=%s", fun_name(), vnfm_info)
        ret = restcall.call_req(
            base_url=ignorcase_get(vnfm_info, 'url'),
            user=ignorcase_get(vnfm_info, 'userName'),
            passwd=ignorcase_get(vnfm_info, 'password'),
            auth_type=restcall.rest_no_auth,
            resource=operation_status_url.format(jobId=ignorcase_get(kwargs, 'jobid'), nfvoId=1,
                                                 vnfmId=ignorcase_get(kwargs, 'vnfmid'),
                                                 responseId=ignorcase_get(kwargs, 'responseId')),
            method='get',
            content=json.JSONEncoder().encode(data)
        )

        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        resp_data = json.JSONDecoder().decode(ret[1])
        logger.info("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred when getting operation status information.")
        raise e
    return Response(data=resp_data, status=status.HTTP_202_ACCEPTED)


# Grant VNF Lifecycle Operation
grant_vnf_url = 'openoapi/nslcm/v1/grantvnf'
grant_vnf_param_map = {
    "VNFMID": "",
    "NFVOID": "",
    "VIMID": "",
    "ExVIMIDList": "",
    "ExVIMID": "",
    "Tenant": "",
    "VNFInstanceID": "vnfInstanceId",
    "OperationRight": "",
    "VMList": "",
    "VMFlavor": "",
    "VMNumber": ""
}


@api_view(http_method_names=['PUT'])
def grantvnf(request, *args, **kwargs):
    logger.info("=====grantvnf=====")
    try:
        resp_data = {}
        logger.info("req_data = %s", request.data)
        data = mapping_conv(grant_vnf_param_map, request.data)
        logger.info("grant_vnf_url = %s", grant_vnf_url)
        data["vnfDescriptorId"] = ""
        if ignorcase_get(request.data, "operationright") == 0:
            data["lifecycleOperation"] = "Instantiate"
            data["addresource"] = []
            for vm in ignorcase_get(request.data, "vmlist"):
                for i in range(int(ignorcase_get(vm, "vmnumber"))):
                    data["addresource"].append(
                        {"type": "vdu",
                         "resourceDefinitionId": i,
                         "vdu": ignorcase_get(vm, "vmflavor"),
                         "vimid": ignorcase_get(vm, "vimid"),
                         "tenant": ignorcase_get(vm, "tenant")
                         })

        data["additionalparam"] = {}
        data["additionalparam"]["vnfmid"] = ignorcase_get(request.data, "vnfmid")
        data["additionalparam"]["vimid"] = ignorcase_get(request.data, "vimid")
        data["additionalparam"]["tenant"] = ignorcase_get(request.data, "tenant")

        logger.info("data = %s", data)
        ret = req_by_msb(grant_vnf_url, "POST",content=json.JSONEncoder().encode(data))
        logger.info("ret = %s", ret)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        resp = json.JSONDecoder().decode(ret[1])

        resp_data['vimid'] = ignorcase_get(resp['vim'], 'vimid')
        resp_data['tenant'] = ignorcase_get(ignorcase_get(resp['vim'], 'accessinfo'), 'tenant')

        logger.info("[%s]resp_data=%s", fun_name(), resp_data)
    except Exception as e:
        logger.error("Error occurred in Grant VNF.")
        raise e
    return Response(data=resp_data, status=status.HTTP_202_ACCEPTED)


# Notify LCM Events
notify_url = 'openoapi/nslcm/v1/vnfs/{vnfInstanceId}/Notify'
notify_param_map = {
    "NFVOID": "",
    "VNFMID": "",
    "VIMID": "vimid",
    "VNFInstanceID": "vnfInstanceId",
    "TimeStamp": "",
    "EventType": "operation",
    "VMList": "",
    "VMFlavor": "",
    "VMNumber": "",
    "VMIDlist": "",
    "VMUUID": "",
}


@api_view(http_method_names=['POST'])
def notify(request, *args, **kwargs):
    try:
        logger.info("[%s]req_data = %s", fun_name(), request.data)
        data = mapping_conv(notify_param_map, request.data)
        logger.info("[%s]data = %s", fun_name(), data)
        ret = req_by_msb(notify_url.format(vnfInstanceId=ignorcase_get(kwargs, 'vnfinstanceid')), "POST",
                         content=json.JSONEncoder().encode(data))
        logger.info("[%s]data = %s", fun_name(), ret)
        if ret[0] != 0:
            return Response(data={'error': ret[1]}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    except Exception as e:
        logger.error("Error occurred in LCM notification.")
        raise e
    return Response(data=None, status=status.HTTP_202_ACCEPTED)
