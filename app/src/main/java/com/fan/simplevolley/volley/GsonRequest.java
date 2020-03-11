package com.fan.simplevolley.volley;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2020/3/11 18:06
 * @Modify:
 */
public class GsonRequest<T> extends Request<T> {


    Gson gson = new Gson();

    Type type;

    /**
     * constructor
     *
     * @param method
     * @param url
     * @param listener
     */
    public GsonRequest(Method method, String url, Response.RequestListener<T> listener, Type type) {
        super(method, url, listener);
        this.type = type;
    }

    @Override
    T parseResponse(Response<T> response) {
        return null;
    }

    @Override
    void parseResponse2(Response<T> response) {
        String jsonStr = new String(response.rawData);
        T t = gson.fromJson(jsonStr, type);
        response.setResult(t);
    }

    @Override
    void deliverResponse(T response) {

        if(listener!= null) {
            listener.onSuccess(response);
        }
    }
}
