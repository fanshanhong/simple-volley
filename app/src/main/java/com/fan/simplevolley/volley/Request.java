package com.fan.simplevolley.volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 请求的基类封装
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public abstract class Request<T> implements Comparable<Request<T>> {

    /**
     * 请求的URL
     */
    public String url;

    /**
     * 请求的方法
     */
    private Method method;

    /**
     * 默认的参数编码
     */
    private String DEFAULT_PARAMS_ENCODING = "UTF-8";

    /**
     * 请求的序列号,用于在请求队列中排序
     */
    private int sequenceNumber;

    /**
     * 请求的优先级,用于在请求队列中排序
     */
    private Priority priority;


    /**
     * 请求头
     */
    Map<String, String> headers = new HashMap<>();

    /**
     * 请求的参数
     */
    Map<String, String> params = new HashMap<>();

    /**
     * 请求结果回调
     */
    Response.RequestListener<T> listener;


    /**
     * constructor
     *
     * @param method
     * @param url
     * @param listener
     */
    public Request(Method method, String url, Response.RequestListener<T> listener) {
        this.method = method;
        this.url = url;
        this.listener = listener;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Method getHttpMethod() {
        return method;
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public Priority getPriority() {
        return priority;
    }

    /**
     * 指定请求时的Content-Type
     *
     * @return
     */
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }


    /**
     * 如果是GET请求, 参数拼接在URL后面, 并且没有请求实体
     * 如果是POST请求, 请求参数是放在请求实体里面
     * <p>
     * 该方法用于获取指定的请求参数, 并将这些参数按照指定格式编码, 生成字节数组。  对于POST 和 PUT请求, 该字节数组的内容将作为请求实体发送
     *
     * @return
     */
    public byte[] getBody() {
        // 这里会调用getParams()方法, 获取指定的参数
        // 多态, 向 RequestQueue 中 add 了Request的子类, 如果子类重写了该方法, 就调用子类的该方法
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    /**
     * 将参数转换为Url编码的参数串, 也就是 key=value&key2=value2的形式, 但是要注意用URLEncoder编码一下
     * 如果请求以这种形式作为请求实体进行请求, 需要将 Content-Type 指定为 application/x-www-form-urlencoded
     */
    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    /**
     * 将 response 解析成需要的数据对象
     *
     * @param response
     * @return
     */
    abstract T parseResponse(Response<T> response);

    abstract void parseResponse2(Response<T> response);

    /**
     * 将解析后的数据对象投递到 UI 线程
     *
     * @param response
     */
    abstract void deliverResponse(T response);

    /**
     * 投递错误
     *
     * @param error
     */
    void deliverError(VolleyError error) {
        if (listener != null) {
            listener.onError(error);
        }
    }

    @Override
    public int compareTo(Request<T> another) {
        Priority myPriority = this.getPriority();
        Priority anotherPriority = another.getPriority();
        // 如果优先级相等,那么按照添加到队列的序列号顺序来执行
        return myPriority.equals(another) ? this.getSequenceNumber() - another.getSequenceNumber()
                : myPriority.ordinal() - anotherPriority.ordinal();
    }


    /**
     * 请求的方法枚举
     */
    public enum Method {
        GET("GET"),
        POST("POST");

        private String method = "";

        private Method(String method) {
            this.method = method;
        }

        @Override
        public String toString() {
            return method;
        }
    }

    /**
     * 请求的优先级枚举
     */
    public enum Priority {
        LOW,
        NORMAL,
        HIGH
    }

}
