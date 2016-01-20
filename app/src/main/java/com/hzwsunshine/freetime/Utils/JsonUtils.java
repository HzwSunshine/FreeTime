package com.hzwsunshine.freetime.Utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 使用Gson解析Json数据
 * Created by He Zhiwei on 2015/7/27.
 */
public class JsonUtils {

    public static <T> List<T> jsonObject2list(String jsonString, final Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
            Log.i("json", "Json解析为List时异常！");
        }
        return list;
    }

    public static <T> List<T> jsonArray2list(String jsonString, final Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();//创建一个JsonParser
            //通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
            JsonElement element = parser.parse(jsonString);
            //把JsonElement对象转换成JsonObject
            JsonArray jsonArray;
            if (element.isJsonArray()) {
                jsonArray = element.getAsJsonArray();
                Iterator<JsonElement> it = jsonArray.iterator();
                JsonElement jsonElement;
                while (it.hasNext()) {
                    jsonElement = it.next();
                    jsonString = jsonElement.toString();
                    list.add(gson.fromJson(jsonString, cls));
                }
            }
            return list;
        } catch (Exception e) {
            Log.i("json", "Json解析为List时异常！");
        }
        return null;
    }

}
