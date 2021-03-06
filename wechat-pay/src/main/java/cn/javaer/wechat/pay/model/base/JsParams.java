/*
 * Copyright (c) 2018 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.javaer.wechat.pay.model.base;

import cn.javaer.wechat.pay.util.ObjectUtils;
import cn.javaer.wechat.pay.util.SignUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * JSAPI 支付需要的参数.
 *
 * @author zhangpeng
 */
@Data
public class JsParams {
    private String appId;
    private String timeStamp;
    private String nonceStr;
    @JsonProperty("package")
    private String packageStr;
    private SignType signType;
    private String paySign;

    private JsParams() {}

    /**
     * 公众号支付时前端 js 所需要的参数.
     *
     * @param prepayId the prepay id
     * @param signType the sign type, signType 必须与下单的一致
     * @param appId 公众号 ID
     * @param mchKey 商户秘钥
     *
     * @return the js params
     */
    public static JsParams create(final String prepayId,
                                  final SignType signType,
                                  final String appId,
                                  final String mchKey) {
        final JsParams params = new JsParams();
        params.appId = appId;
        params.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        params.nonceStr = ObjectUtils.uuid32();
        params.packageStr = "prepay_id=" + prepayId;
        params.signType = signType == null ? SignType.MD5 : signType;
        final SortedMap<String, String> paramsMap = new TreeMap<>();
        paramsMap.put("appId", params.appId);
        paramsMap.put("timeStamp", params.timeStamp);
        paramsMap.put("nonceStr", params.nonceStr);
        paramsMap.put("package", params.packageStr);
        paramsMap.put("signType", params.signType.toString());
        params.paySign = SignUtils.generateSign(paramsMap, mchKey);
        return params;
    }
}
