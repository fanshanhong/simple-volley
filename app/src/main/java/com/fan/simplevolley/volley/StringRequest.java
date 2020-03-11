package com.fan.simplevolley.volley;

import java.io.UnsupportedEncodingException;

/**
 * @Description:
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public class StringRequest extends Request<String> {

    public StringRequest(Method method, String url, Response.RequestListener listener) {
        super(method, url, listener);
    }

    @Override
    String parseResponse(Response<String> response) {
        return new String(response.rawData);
    }

    @Override
    void parseResponse2(Response<String> response) {
        String rowDataStr = new String(response.rawData);


        Response<String> success = response.success(rowDataStr);

        // 或者
        response.setResult(rowDataStr);
    }

    @Override
    void deliverResponse(String response) {
        listener.onSuccess(response);
    }
}
