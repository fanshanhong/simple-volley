package com.fan.simplevolley.volley;

import android.os.Handler;
import android.os.Looper;

/**
 * @Description: 将响应数据从工作线程投递到UI线程的投递者。使用 Android Handler来实现
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public class Delivery {

    Handler handler = new Handler(Looper.getMainLooper());

    public void postResponse(final Request request, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                // Deliver a normal response or error, depending.
                // 成功, 就去调用request设置好的成功回调
                // 这里deliverResponse 只是在  request.callback上又包了一层, 无其他
                request.deliverResponse(response.result);
            }
        });
    }

    public void postError(final Request request, final VolleyError error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                request.deliverError(error);
            }
        });
    }
}
