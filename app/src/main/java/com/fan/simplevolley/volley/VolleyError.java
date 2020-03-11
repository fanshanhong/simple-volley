package com.fan.simplevolley.volley;

/**
 * @Description:
 * @Author: fan
 * @Date: 2020/3/11 17:45
 * @Modify:
 */
public class VolleyError extends Exception{

    public VolleyError() {
    }

    public VolleyError(String message) {
        super(message);
    }

    public VolleyError(String message, Throwable cause) {
        super(message, cause);
    }

    public VolleyError(Throwable cause) {
        super(cause);
    }

}
