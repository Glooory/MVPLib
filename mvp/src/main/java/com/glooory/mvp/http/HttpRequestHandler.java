package com.glooory.mvp.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Glooory on 17/5/3.
 */

public interface HttpRequestHandler {

    Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response);

    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);

    HttpRequestHandler EMPTY_HTTP_REQUEST_HANDLER = new HttpRequestHandler() {
        @Override
        public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain,
                Response response) {
            // 不管是否处理，都必须将 response 返回回去
            return response;
        }

        @Override
        public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
            // 不管是否处理，都必须将 request 返回回去
            return request;
        }
    };

}
