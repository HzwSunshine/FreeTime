package com.hzwsunshine.freetime.Utils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.hzwsunshine.freetime.Application.Application;
import com.hzwsunshine.freetime.Application.VolleySingleton;

import java.util.Map;


/**
 * 网络访问工具类
 * Created by He Zhiwei on 2015/7/27.
 */
public class HttpUtils {

    /**
     * get请求
     */
    public static void get(String url, ResponseUtils response) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response, response);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 1, 1.0f));//15秒超时
        VolleySingleton.getVolleySingleton(Application.getContent()).addToRequestQueue(stringRequest);
    }

    /**
     * post请求
     */
    public static void post(String url, final Map<String, String> paramsMap, ResponseUtils response) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response, response) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paramsMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 1, 1.0f));//15秒超时
        VolleySingleton.getVolleySingleton(Application.getContent()).addToRequestQueue(stringRequest);
    }

}