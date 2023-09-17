package xin.ryven.flashsale.order.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xin.ryven.flashsale.order.entity.FlashSale;
import xin.ryven.flashsale.order.entity.SuccessSale;
import xin.ryven.flashsale.order.exception.DuplicatedSaleException;
import xin.ryven.flashsale.order.exception.SaleConcurrentException;
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
            int updated = flashSaleRepository.reduceQuantity(id, sale.getQuantity() - quantity, sale.getVersion());
            if (updated == 0) {
                throw new SaleConcurrentException("下单人数多，请重试");
            }
            try {
                successSaleRepository.save(buildSuccessSale(id, phone));
            } catch (DataIntegrityViolationException e) {
                throw new DuplicatedSaleException("重复下单", e);
            }

        } else {
            throw new UnderStockException("库存不足");
        }
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String flashSaleV2(Long id, Integer quantity, String phone) {
        int updated = flashSaleRepository.reduceQuantity(id, quantity);
        if (updated > 0) {
            try {
                successSaleRepository.save(buildSuccessSale(id, phone));
            } catch (DataIntegrityViolationException e) {
                throw new DuplicatedSaleException("重复下单", e);
            }
        } else {
            throw new UnderStockException("库存不足");
        }
        return "success";
    }

    private SuccessSale buildSuccessSale(Long flashSaleId, String phone) {
        return SuccessSale.builder()
                .flashSaleId(flashSaleId)
                .createTime(new Date())
                .state(1)
                .userPhone(phone).build();
    }
}
