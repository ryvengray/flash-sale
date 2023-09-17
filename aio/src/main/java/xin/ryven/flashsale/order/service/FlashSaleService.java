package xin.ryven.flashsale.order.service;

import xin.ryven.flashsale.order.entity.FlashSale;

import java.util.List;

public interface FlashSaleService {

    List<FlashSale> list();

    FlashSale getById(Long id);

    String flashSale(Long id, Integer quantity, String phone);
}
