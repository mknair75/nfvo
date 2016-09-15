# Copyright 2016 [ZTE] and others.
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
import logging

from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView

from lcm.ns.vnfs import create_vnfs
from lcm.ns.vnfs.create_vnfs import CreateVnfs
from lcm.ns.vnfs.get_vnfs import GetVnf
from lcm.ns.vnfs.terminate_nfs import TerminateVnfs
from lcm.ns.vnfs.grant_vnfs import GrantVnfs
from lcm.ns.vnfs.notify_lcm import NotifyLcm
from lcm.pub.database.models import NfInstModel
from lcm.pub.utils.jobutil import JobUtil, JOB_TYPE
from lcm.pub.utils.values import ignore_case_get

logger = logging.getLogger(__name__)


class NfView(APIView):
    def post(self, request):
        print "VnfCreateView--post::> %s" % request.data
        data = {'ns_instance_id': ignore_case_get(request.data, 'nsInstanceId'),
                'additional_param_for_ns': ignore_case_get(request.data, 'additionalParamForNs'),
                'additional_param_for_vnf': ignore_case_get(request.data, 'additionalParamForVnf'),
                'vnf_index': ignore_case_get(request.data, 'vnfIndex')}
        nf_inst_id, job_id = create_vnfs.prepare_create_params()
        CreateVnfs(data, nf_inst_id, job_id).start()
        rsp = {
            "vnfInstId": nf_inst_id,
            "jobId": job_id
        }
        return Response(data=rsp, status=status.HTTP_202_ACCEPTED)


class NfDetailView(APIView):
    def get(self, request, vnfinstid):
        print "VnfQueryView--get::> %s" % vnfinstid
        nf_inst_info = GetVnf(vnfinstid).do_biz()
        if not nf_inst_info:
            return Response(status=status.HTTP_404_NOT_FOUND)
        return Response(status=status.HTTP_200_OK,
                        data={'vnfInstId': nf_inst_info[0].nfinstid, 'vnfName': nf_inst_info[0].nf_name,
                              'vnfStatus': nf_inst_info[0].status})

    def post(self, request_paras, vnfinstid):
        vnf_inst_id = vnfinstid
        terminationType = ignore_case_get(request_paras.data, 'terminationType')
        gracefulTerminationTimeout = ignore_case_get(request_paras.data, 'gracefulTerminationTimeout')
        job_id = JobUtil.create_job("VNF", JOB_TYPE.TERMINATE_VNF, vnf_inst_id)
        data = {'terminationType': terminationType, 'gracefulTerminationTimeout': gracefulTerminationTimeout}
        TerminateVnfs(data, vnf_inst_id, job_id).run()
        rsp = {'jobId': job_id}
        return Response(data=rsp, status=status.HTTP_201_CREATED)


class NfGrant(APIView):
    def post(self, request):
        try:
            vnf_inst_id = ignore_case_get(request.data, 'vnfInstanceId')
            job_id = JobUtil.create_job("VNF", JOB_TYPE.GRANT_VNF, vnf_inst_id)
            rsp = GrantVnfs(request.data, job_id).send_grant_vnf_to_resMgr()
            return Response(data=rsp, status=status.HTTP_201_CREATED)
        except Exception as e:
            return Response(data={'error': '%s' % e.message}, status=status.HTTP_409_CONFLICT)


class LcmNotify(APIView):
    def post(self, request_paras, vnfmid, vnfInstanceId):
        try:
            NotifyLcm(vnfmid, vnfInstanceId, request_paras.data).do_biz()
            return Response(data={}, status=status.HTTP_201_CREATED)
        except Exception as e:
            return Response(data={'error': '%s' % e.message}, status=status.HTTP_409_CONFLICT)