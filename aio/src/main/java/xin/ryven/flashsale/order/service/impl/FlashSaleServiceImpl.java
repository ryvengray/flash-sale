package xin.ryven.flashsale.order.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xin.ryven.flashsale.order.entity.FlashSale;
import xin.ryven.flashsale.order.entity.SuccessSale;
import xin.ryven.flashsale.order.exception.UnderStockException;
import xin.ryven.flashsale.order.repository.FlashSaleRepository;
import xin.ryven.flashsale.order.repository.SuccessSaleRepository;
import xin.ryven.flashsale.order.service.FlashSaleService;

import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public FlashSale getById(Long id) {
        return flashSaleRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String flashSale(Long id, Integer quantity, String phone) {
        FlashSale sale = flashSaleRepository.findById(id).orElseThrow();
        if (sale.getQuantity() >= quantity) {
            flashSaleRepository.reduceQuantity(id, sale.getQuantity() - quantity);
            successSaleRepository.save(SuccessSale.builder()
                    .flashSaleId(id)
                    .createTime(new Date())
                    .state(1)
                    .userPhone(phone).build());
        } else {
            throw new UnderStockException("库存不足");
        }
        return "success";
    }
}
