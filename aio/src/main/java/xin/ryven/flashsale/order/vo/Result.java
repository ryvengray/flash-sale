package xin.ryven.flashsale.order.vo;

import lombok.Data;

@Data
public class Result<T> {

    private int errorCode;

    private String msg;

    private T data;

    public static <T> Result<T> success() {
        Result<T> r = new Result<>();
        r.errorCode = 0;
        r.msg = "success";
        return r;
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.errorCode = 0;
        r.msg = "success";
        r.data = data;
        return r;
    }
}
