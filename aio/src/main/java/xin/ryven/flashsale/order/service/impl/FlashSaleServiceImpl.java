package xin.ryven.flashsale.order.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
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

import java.util.*;

@Service
@Slf4j
public class FlashSaleServiceImpl implements FlashSaleService {

    private final FlashSaleRepository flashSaleRepository;

    private final SuccessSaleRepository successSaleRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    public FlashSaleServiceImpl(FlashSaleRepository flashSaleRepository, SuccessSaleRepository successSaleRepository, RedisTemplate<String, Object> redisTemplate) {
        this.flashSaleRepository = flashSaleRepository;
        this.successSaleRepository = successSaleRepository;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        // 初始化库存量
        refreshStock();
    }

    public void refreshStock() {
        flashSaleRepository.findAll().forEach(fs -> redisTemplate.opsForValue().set(redisKey(fs.getId()), fs.getQuantity().toString()));
        log.info("Finish sync stock to redis");
    }

    private String redisKey(Long id) {
        return "flash:sale:quantity:" + id;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String flashSaleV3(Long id, Integer quantity, String phone) {
        boolean decreaseStock = decreaseStock(id, quantity);
        if (decreaseStock) {
            try {
                successSaleRepository.save(buildSuccessSale(id, phone));
            } catch (DataIntegrityViolationException e) {
                // 重新加回去
                log.warn("库存回滚: {}", id);
                decreaseStock(id, -quantity);
                throw new DuplicatedSaleException("重复下单", e);
            }
        } else {
            throw new UnderStockException("库存不足");
        }
        return "success";
    }

    private boolean decreaseStock(Long id, Integer quantity) {
        RedisScript<Long> script = new DefaultRedisScript<>(
                "if redis.call('GET', KEYS[1]) >= ARGV[1] then\n" +
                        "    return redis.call('DECRBY', KEYS[1], ARGV[1])\n" +
                        "else\n" +
                        "    return -1\n" +
                        "end",
                Long.class
        );
        String key = redisKey(id);
        Long result = redisTemplate.execute(script, Collections.singletonList(key), quantity.toString());
        return result >= 0;
    }

    private SuccessSale buildSuccessSale(Long flashSaleId, String phone) {
        return SuccessSale.builder()
                .flashSaleId(flashSaleId)
                .createTime(new Date())
                .state(1)
                .userPhone(phone).build();
    }
}
