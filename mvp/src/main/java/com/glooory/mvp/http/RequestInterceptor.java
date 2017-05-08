package com.glooory.mvp.http;

import com.glooory.mvp.util.ZipHelper;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
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

        boolean hasRequestBody = request.body() != null;

        Buffer requestBuffer = new Buffer();

        if (hasRequestBody) {
            request.body().writeTo(requestBuffer);
        }

        long timeRequestStart = System.nanoTime();
        Response originalResponse = null;
        try {
            originalResponse = chain.proceed(request);
        } catch (Exception e) {
            Logger.d("Http Error: " + e);
            e.printStackTrace();
        }

        long timeRequestEnd = System.nanoTime();

        String bodySize = originalResponse.body().contentLength() != -1 ?
                originalResponse.body().contentLength() + " bytes" : "unknown length";

        Logger.d("Response:\n" +
                "    Response Time:" + String.valueOf(timeRequestEnd - timeRequestStart) + "ms" +
                "    Response Size" + bodySize +
                "    Headers:" + originalResponse.headers());

        String bodyString = printResponse(request, originalResponse);

        if (mHttpRequestHandler != null) {
            return mHttpRequestHandler.onHttpResultResponse(bodyString, chain, originalResponse);
        }

        return originalResponse;
    }

    private String printResponse(Request request, Response originalResponse) throws IOException {
        // 读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();
        String bodyString = null;
        if (isParseable(responseBody)) {
            BufferedSource bufferedSource = responseBody.source();
            bufferedSource.request(Long.MAX_VALUE);
            Buffer buffer = bufferedSource.buffer();

            // 获取 content 的压缩类型
            String encoding = originalResponse.headers().get("Content-Encoding");

            Buffer clone = buffer.clone();

            // 解析 Response Content
            bodyString = parseResponseContent(responseBody, encoding, clone);

            Logger.d("Response:\n" + bodyString);
        } else {
            Logger.d("Response:\n" + "The Response can not be parsed");
        }
        return bodyString;
    }

    private String parseResponseContent(ResponseBody responseBody, String encoding, Buffer clone) {
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) { // Content 使用 gzip 压缩
            return ZipHelper.decompressForGzip(clone.readByteArray(), convertCharset(charset));
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) { // Content 使用 Zlib 压缩
            return ZipHelper.decompressToStringForZlib(clone.readByteArray(), convertCharset(charset));
        } else { // Content 没有被压缩
            return clone.readString(charset);
        }
    }

    public static boolean isParseable(ResponseBody responseBody) {
        if (responseBody.contentLength() == 0) {
            return false;
        }
        return responseBody.contentType().toString().contains("text") || isJson(responseBody);
    }

    public static boolean isJson(ResponseBody responseBody) {
        return responseBody.contentType().toString().contains("json");
    }

    public static String convertCharset(Charset charset) {
        String s = charset.toString();
        int i = s.indexOf("[");
        if (i == -1) {
            return s;
        }
        return s.substring(i + 1, s.length() - 1);
    }

}
