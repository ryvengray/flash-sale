package xin.ryven.flashsale.order.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import xin.ryven.flashsale.order.entity.FlashSale;

public interface FlashSaleRepository extends CrudRepository<FlashSale, Long> {

    @Modifying
    @Query(value = "update FlashSale set quantity = :quantity, version = version + 1 where id = :id and version = :version")
    int reduceQuantity(@Param("id") Long id, @Param("quantity") Integer quantity, @Param("version") Integer version);

    @Modifying
    @Query(value = "update FlashSale set quantity = quantity - :quantity, version = version + 1 where id = :id and quantity >= :quantity")
    int reduceQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);
}
