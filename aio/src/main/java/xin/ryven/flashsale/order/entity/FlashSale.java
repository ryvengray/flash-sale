package xin.ryven.flashsale.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "flash_sale", schema = "fs")
public class FlashSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer quantity;

    private Integer version;

    private Date startTime;

    private Date endTime;

    private Date createTime;

}
