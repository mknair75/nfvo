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

package org.openo.nfvo.jujuvnfmadapter.service.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openo.nfvo.jujuvnfmadapter.common.StringUtil;

import mockit.Mock;
import mockit.MockUp;


/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author		quanzhong@huawei.com
 * @version     NFVO 0.5  Nov 2, 2016
 */
public class TestMockUp {
    ConfigRoa roa;
    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since  NFVO 0.5
     */
    @Before
    public void setUp() throws Exception {
        roa = new ConfigRoa();       
    }

    @Test
    public void test() {
        new MockUp<StringUtil>(){
            @Mock
            public boolean isValidUrl(String url) {
                return true;
            }
            @Mock
            public boolean is2ValidUrl(String url) {
                return true;
            }
        };
        Assert.assertTrue(StringUtil.isValidUrl("abc"));
    }

}
