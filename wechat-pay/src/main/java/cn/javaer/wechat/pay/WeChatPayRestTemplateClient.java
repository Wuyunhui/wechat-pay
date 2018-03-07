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

package cn.javaer.wechat.pay;

import cn.javaer.wechat.pay.model.BasePayResponse;
import cn.javaer.wechat.pay.model.CloseOrderRequest;
import cn.javaer.wechat.pay.model.CloseOrderResponse;
import cn.javaer.wechat.pay.model.OrderQueryRequest;
import cn.javaer.wechat.pay.model.OrderQueryResponse;
import cn.javaer.wechat.pay.model.RefundQueryRequest;
import cn.javaer.wechat.pay.model.RefundQueryResponse;
import cn.javaer.wechat.pay.model.RefundRequest;
import cn.javaer.wechat.pay.model.RefundResponse;
import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * 微信支付客户端-RestTemplate实现.
 *
 * @author zhangpeng
 */
public class WeChatPayRestTemplateClient implements WeChatPayClient {

    private final RestTemplate restTemplate;

    /**
     * Instantiates a new WeChatPayRestTemplateClient.
     *
     * @param restTemplate RestTemplate
     */
    public WeChatPayRestTemplateClient(
            final RestTemplate restTemplate) {
        Objects.requireNonNull(restTemplate);

        this.restTemplate = restTemplate;
    }

    @Override
    public UnifiedOrderResponse unifiedOrder(final UnifiedOrderRequest request) throws WeChatPayException {
        Objects.requireNonNull(request);
        return postForEntity(WeChatPayClient.UNIFIED_ORDER_PATH, request, UnifiedOrderResponse.class);
    }

    @Override
    public OrderQueryResponse orderQuery(final OrderQueryRequest request) throws WeChatPayException {
        Objects.requireNonNull(request);
        return postForEntity(WeChatPayClient.ORDER_QUERY_PATH, request, OrderQueryResponse.class);
    }

    @Override
    public CloseOrderResponse closeOrder(final CloseOrderRequest request) throws WeChatPayException {
        Objects.requireNonNull(request);
        return postForEntity(WeChatPayClient.CLOSE_ORDER_PATH, request, CloseOrderResponse.class);
    }

    @Override
    public RefundResponse refund(final RefundRequest request) throws WeChatPayException {
        Objects.requireNonNull(request);

        return postForEntity(WeChatPayClient.REFUND_PATH, request, RefundResponse.class);
    }

    @Override
    public RefundQueryResponse refundQuery(final RefundQueryRequest request) throws WeChatPayException {
        Objects.requireNonNull(request);
        return postForEntity(WeChatPayClient.REFUND_QUERY_PATH, request, RefundQueryResponse.class);
    }

    private <Q, S extends BasePayResponse> S postForEntity(final String apiPath, final Q request, final Class<S> responseClass) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        final HttpEntity<Q> httpEntity = new HttpEntity<>(request, headers);
        final String url = WeChatPayUtils.joinPath(WeChatPayConfigurator.DEFAULT.getApiBasePath(), apiPath);
        final S response = this.restTemplate.postForEntity(url, httpEntity, responseClass).getBody();
        response.checkSignAndSuccessful();
        return response;
    }
}
