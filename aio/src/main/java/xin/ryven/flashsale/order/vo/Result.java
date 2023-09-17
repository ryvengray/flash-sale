package xin.ryven.flashsale.order.vo;

import lombok.Data;

@Data
public class Result {

    private int errorCode;

    private String msg;

    private Object data;

    public static Result success() {
        Result r = new Result();
        r.errorCode = 0;
        r.msg = "success";
        return r;
    }

    public static Result fail(int errorCode, String msg) {
        Result r = new Result();
        r.errorCode = errorCode;
        r.msg = msg;
        return r;
    }

    public static Result success(Object data) {
        Result r = new Result();
        r.errorCode = 0;
        r.msg = "success";
        r.data = data;
        return r;
    }
}
