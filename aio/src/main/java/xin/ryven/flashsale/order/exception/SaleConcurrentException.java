package xin.ryven.flashsale.order.exception;

public class SaleConcurrentException extends BaseSaleException {

    public SaleConcurrentException(String s) {
        super(s);
    }

    @Override
    public int errorCode() {
        return 4;
    }
}
