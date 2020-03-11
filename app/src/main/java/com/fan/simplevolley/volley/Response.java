package com.fan.simplevolley.volley;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;

/**
 * @Description: 响应的封装类
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public class Response<T> extends BasicHttpResponse {

    /**
     * 响应实体数据
     */
    byte[] rawData;

    /**
     * 将响应实体解析后的对象
     */
    public T result;

    public Response(StatusLine statusline) {
        super(statusline);
    }

    public Response(ProtocolVersion ver, int code, String reason) {
        super(ver, code, reason);
    }

    public Response<T> success(T parsed) {
        result = parsed;
        return this;
    }

    public void setData(byte[] rowData) {
        this.rawData = rowData;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public interface RequestListener<T> {

        void onSuccess(T result);

        void onError(VolleyError error);

    }
}
