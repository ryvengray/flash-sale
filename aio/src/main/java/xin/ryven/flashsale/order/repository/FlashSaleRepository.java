package xin.ryven.flashsale.order.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import xin.ryven.flashsale.order.entity.FlashSale;

public interface FlashSaleRepository extends CrudRepository<FlashSale, Long> {

    @Modifying
    @Query(value = "update FlashSale set quantity = :quantity where id = :id")
    void reduceQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);
}
