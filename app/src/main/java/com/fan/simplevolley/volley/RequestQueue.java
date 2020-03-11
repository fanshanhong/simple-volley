package com.fan.simplevolley.volley;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * @Description: 请求队列封装
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public class RequestQueue {

    /**
     * 阻塞的请求队列
     */
    PriorityBlockingQueue<Request<?>> blockingQueue = new PriorityBlockingQueue<>();

    /**
     * 每一个请求的序列号生成器
     */
    AtomicInteger sequenceNumberGenerator = new AtomicInteger();

    /**
     * 默认执行网络请求的线程数
     */
    public static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;

    /**
     * 自己维护的网络请求线程
     */
    NetworkDispatcher[] networkDispatchers;

    /**
     * 真正执行网络请求的东东
     */
    HttpStack httpStack;

    /**
     * constructor
     *
     * @param threadPoolSize
     * @param httpStack
     */
    public RequestQueue(int threadPoolSize, HttpStack httpStack) {
        networkDispatchers = new NetworkDispatcher[DEFAULT_NETWORK_THREAD_POOL_SIZE];
        this.httpStack = httpStack != null ? httpStack : new UrlHttpStack();
    }


    public void add(Request<?> request) {
        if (!blockingQueue.contains(request)) {
            blockingQueue.add(request);
        } else {

            System.out.println("请求已经在队列中, 请不要重复add");
        }
    }

    public void start() {

        stop();

        for (int i = 0; i < DEFAULT_NETWORK_THREAD_POOL_SIZE; i++) {
            networkDispatchers[i] = new NetworkDispatcher(blockingQueue, httpStack);
            networkDispatchers[i].start();
        }

    }

    public void stop() {

        if (networkDispatchers!= null)
        for (int i = 0; i < networkDispatchers.length; i++) {
            if (networkDispatchers[i] != null) {
                networkDispatchers[i].quit();
            }
        }
    }
}
