package com.fan.simplevolley.volley;

import java.util.concurrent.BlockingQueue;

/**
 * @Description: 网络请求线程, 其主要工作是:
 * 1. 从请求队列中取出Request(需要持有RequestQueue 中的 blockingQueue 的引用)
 * 2. 使用HttpStack执行网络请求并拿到响应结果 (需要持有HttpStack的引用)
 * 3. 将响应结果投递要UI线程进行处理 (需要一个投递者Delivery)
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public class NetworkDispatcher extends Thread {

    /**
     * 是否退出
     */
    boolean quit;

    /**
     * 真正执行网络请求的
     */
    HttpStack httpStack;

    /**
     * 存放请求的队列
     */
    BlockingQueue<Request<?>> blockingQueue;

    /**
     * 将响应结果投递到UI线程的投递者
     */
    Delivery delivery = new Delivery();

    /**
     * constructor
     *
     * @param blockingQueue
     * @param httpStack
     */
    NetworkDispatcher(BlockingQueue<Request<?>> blockingQueue, HttpStack httpStack) {
        this.httpStack = httpStack;
        this.blockingQueue = blockingQueue;
    }


    @Override
    public void run() {
        // 死循环,  不断从阻塞队列中取出Request 去执行
        while (true) {
            Request<?> request;
            try {
                // Take a request from the queue, 如果没有的话就阻塞了.
                request = blockingQueue.take();
            } catch (InterruptedException e) {
                // We may have been interrupted because it was time to quit.
                if (quit) {
                    return;
                }
                continue;
            }

            Response response = null;
            try {

                // 调用 httpStack 去执行真正的网络请求, 此时, response 中的rawData已经存入了响应的实体
                response = httpStack.performRequest(request);

                // 方式1:
                //Object o = request.parseResponse(response);
                //response.setResult(o);

                // 方式2:
                request.parseResponse2(response);

                delivery.postResponse(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                delivery.postError(request, new VolleyError(e));
            }
        }
    }

    public void quit() {
        quit = true;
        interrupt();
    }
}
