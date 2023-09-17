package xin.ryven.flashsale.order.exception;

public abstract class BaseSaleException extends RuntimeException {

    public BaseSaleException(String s) {
        super(s);
    }

    public BaseSaleException(String s, Throwable t) {
        super(s, t);
    }

    public abstract int errorCode();
}
