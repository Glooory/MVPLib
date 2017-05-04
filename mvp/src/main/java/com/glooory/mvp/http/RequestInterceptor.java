package com.glooory.mvp.http;

import com.glooory.mvp.util.ZipHelper;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Glooory on 17/5/3.
 */

public class RequestInterceptor implements Interceptor {

    private HttpRequestHandler mHttpRequestHandler;

    @Inject
    public RequestInterceptor(HttpRequestHandler httpRequestHandler) {
        this.mHttpRequestHandler = httpRequestHandler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (mHttpRequestHandler != null) {
            // 在请求之前可以拿到 request ，做一些比如添加 Header 等操作
            request = mHttpRequestHandler.onHttpRequestBefore(chain, request);
        }

        Buffer requestBuffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(requestBuffer);
        } else {
            Logger.d("request.body() == null");
        }

        // 打印 url 信息
        Logger.d("Request Info:\n" +
                "    Url:" + request.url() + "\n" +
                "    Params:" + request.body() != null ? parseParams(request.body(), requestBuffer) : "mull" + "\n" +
                "    Connection:" + chain.connection() + "\n" +
                "    Headers:" + request.headers());

        long timeRequestStart = System.nanoTime();
        Response originalResponse = chain.proceed(request);
        long timeRequestEnd = System.nanoTime();
        Logger.d("Response:\n" +
                "    Response Time:" + String.valueOf(timeRequestEnd - timeRequestStart) + "ms" +
                "    Headers:" + originalResponse.headers());

        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

        // Content 的压缩类型
        String encoding = originalResponse
                .headers()
                .get("Content-Encoding");

        Buffer clone = buffer.clone();
        String bodyString;

        // 解析 Response Content
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            // Content 使用 gzip 压缩
            bodyString = ZipHelper.decompressForGzip(clone.readByteArray());
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {
            // Content 使用 zlib 压缩
            bodyString = ZipHelper.decompressToStringForZlib(clone.readByteArray());
        } else {
            // Content 没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }
        Logger.d("Result:\n" + bodyString);

        if (mHttpRequestHandler != null) {
            // 这里可以比客户端提前一步拿到服务器返回的结果，可以做一些其他操作，如 Token 失效重新获取等
            return mHttpRequestHandler.onHttpResultResponse(bodyString, chain, originalResponse);
        }

        return originalResponse;
    }

    public static String parseParams(RequestBody body, Buffer requestBuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8");
        }
        return "null";
    }
}
