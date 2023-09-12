package xin.ryven.flashsale.order.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.ryven.flashsale.order.entity.FlashSale;
import xin.ryven.flashsale.order.service.FlashSaleService;
import xin.ryven.flashsale.order.vo.Result;

import java.util.List;

@RestController
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    @GetMapping("/flash-sale/list")
    public Result<List<FlashSale>> list() {
        return Result.success(flashSaleService.list());
    }
}
