package xin.ryven.flashsale.order.exception;

public class DuplicatedSaleException extends BaseSaleException {

    public DuplicatedSaleException(String s, Throwable r) {
        super(s, r);
    }

    @Override
    public int errorCode() {
        return 1;
    }
}
