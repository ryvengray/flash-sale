package xin.ryven.flashsale.order.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 统一json处理
 *
 * @author gray
 */
public class JsonU {

    public String toStr(Object o) {
        Gson g = new Gson();
        return g.toJson(o);
    }

    public <T> T fromStr(String source, Class<T> clazz) {
        Gson g = new Gson();
        return g.fromJson(source, clazz);
    }

    public <T> T fromStr(String source, TypeToken<T> typeToken) {
        Gson g = new Gson();
        return g.fromJson(source, typeToken.getType());
    }
}
