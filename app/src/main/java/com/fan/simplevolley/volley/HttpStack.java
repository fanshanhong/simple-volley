package com.fan.simplevolley.volley;

/**
 * @Description: 执行网络请求的接口, 其子类需要实现performRequest()方法, 去真正的进行网络请求
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public interface HttpStack {
    public Response<?> performRequest(Request<?> request) throws Exception;
}
