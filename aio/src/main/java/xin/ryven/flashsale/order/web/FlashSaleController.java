package xin.ryven.flashsale.order.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.ryven.flashsale.order.exception.BaseSaleException;
import xin.ryven.flashsale.order.service.FlashSaleService;
import xin.ryven.flashsale.order.vo.Result;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    @GetMapping("/flash-sale/list")
    public Result list() {
        return Result.success(flashSaleService.list());
    }

    @GetMapping("/flash-sale/detail")
    public Result detail(Long id) {
        return Result.success(flashSaleService.getById(id));
    }

    @PostMapping("/flash-sale")
    public Result flashSale(Long id, Integer quantity, String phone) {
        // 8.5sec/m   threads: 500 ramp-up: 2s circulation: 1   100个库存还剩44个
        try {
            return Result.success(flashSaleService.flashSale(id, quantity, phone));
        } catch (BaseSaleException e) {
            log.error("秒杀失败", e);
            return Result.fail(e.errorCode(), e.getMessage());
        }
    }

    @PostMapping("/flash-sale/v2")
    public Result flashSaleV2(Long id, Integer quantity, String phone) {
        // 15.5sec/m   threads: 500 ramp-up: 2s circulation: 1   100个库存全部销售完成
        try {
            return Result.success(flashSaleService.flashSaleV2(id, quantity, phone));
        } catch (BaseSaleException e) {
            log.error("秒杀失败", e);
            return Result.fail(e.errorCode(), e.getMessage());
        }
    }
}
