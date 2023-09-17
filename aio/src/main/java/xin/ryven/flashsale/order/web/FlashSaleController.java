package xin.ryven.flashsale.order.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.ryven.flashsale.order.entity.FlashSale;
import xin.ryven.flashsale.order.service.FlashSaleService;
import xin.ryven.flashsale.order.vo.Result;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    @GetMapping("/flash-sale/list")
    public Result<List<FlashSale>> list() {
        return Result.success(flashSaleService.list());
    }

    @GetMapping("/flash-sale/detail")
    public Result<FlashSale> detail(Long id) {
        return Result.success(flashSaleService.getById(id));
    }

    @PostMapping("/flash-sale")
    public Result<Void> flashSale(Long id, Integer quantity, String phone) {
        try {
            flashSaleService.flashSale(id, quantity, phone);
            return Result.success();

        } catch (Exception e) {
            log.error("秒杀失败", e);
            return Result.fail(e.getMessage());
        }
    }
}
