package xin.ryven.flashsale.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "success_sale", schema = "fs")
public class SuccessSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long flashSaleId;

    private String userPhone;

    private Integer state;

    private Date createTime;

}
