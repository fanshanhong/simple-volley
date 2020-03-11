package com.fan.simplevolley.volley;

/**
 * @Description: 这是一个简单的 volley 网络请求框架的实现。 我称它为 simple-volley。
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public class SimpleVolley {

    /**
     * 创建一个 RequestQueue, 类似 Volley.newRequestQueue();
     * @return
     */
    public static RequestQueue newRequestQueue() {
        return newRequestQueue(RequestQueue.DEFAULT_NETWORK_THREAD_POOL_SIZE);
    }

    /**
     * 创建一个请求队列,NetworkExecutor数量为coreNums
     *
     * @param coreNums
     * @return
     */
    public static RequestQueue newRequestQueue(int coreNums) {
        return newRequestQueue(coreNums, null);
    }

    public static RequestQueue newRequestQueue(int coreNum, HttpStack httpStack) {

        RequestQueue requestQueue = new RequestQueue(coreNum, httpStack);
        requestQueue.start();
        return requestQueue;
    }
}
