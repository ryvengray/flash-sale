package xin.ryven.flashsale.order.service.impl;

import org.springframework.stereotype.Service;
import xin.ryven.flashsale.order.entity.FlashSale;
import xin.ryven.flashsale.order.repository.FlashSaleRepository;
import xin.ryven.flashsale.order.repository.SuccessSaleRepository;
import xin.ryven.flashsale.order.service.FlashSaleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlashSaleServiceImpl implements FlashSaleService {

    private final FlashSaleRepository flashSaleRepository;

    private final SuccessSaleRepository successSaleRepository;

    public FlashSaleServiceImpl(FlashSaleRepository flashSaleRepository, SuccessSaleRepository successSaleRepository) {
        this.flashSaleRepository = flashSaleRepository;
        this.successSaleRepository = successSaleRepository;
    }

    @Override
    public List<FlashSale> list() {
        List<FlashSale> list = new ArrayList<>();
        flashSaleRepository.findAll().forEach(list::add);
        return list;
    }
}
