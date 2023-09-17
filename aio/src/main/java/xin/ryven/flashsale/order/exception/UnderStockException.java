package xin.ryven.flashsale.order.exception;

public class UnderStockException extends BaseSaleException {

    public UnderStockException(String s) {
        super(s);
    }

    @Override
    public int errorCode() {
        return 2;
    }
}
