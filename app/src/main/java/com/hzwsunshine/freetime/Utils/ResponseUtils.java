package com.hzwsunshine.freetime.Utils;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by He Zhiwei on 2015/7/27.
 */
public abstract class ResponseUtils implements Response.Listener<String>, Response.ErrorListener {

    @Override
    public void onResponse(String response) {
        try{
            success(response);
        }catch (Exception e){
            Log.i("xxx","返回数据处理异常信息："+e.toString());
            e.printStackTrace();
        }
    }

    public abstract void success(String response);

    @Override
    public void onErrorResponse(VolleyError error) {
        volleyErrorInfo(error);
        failure();
    }

    /**
     * 该方法为获取Json数据失败时的回调，该方法中不做任何网络请求错误之类的错误提示
     * 仅仅处理请求错误后的逻辑
     * 起初为抽象方法，后来改为普通方法，只在有处理错误需要的时候才去调用此方法，不强制重写此方法
     */
    public void failure(){}

    /**
     * 获取Json数据失败时的错误提示
     * @param error
     */
    private void volleyErrorInfo(VolleyError error) {
        String errors = error.toString();
        if (errors.contains("NoConnectionError")) {
//            ViewUtils.showToast("好像断网了...");
        } else if (errors.contains("SERVERERROR")) {
            ViewUtils.showToast("服务器未响应");
        } else if (errors.contains("TimeoutError")) {
            ViewUtils.showToast("网络连接超时");
        }
    }

}

