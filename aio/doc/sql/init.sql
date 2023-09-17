-- 创建数据库
create database if not exists db_flash_sale;
-- 使用数据库
use db_flash_sale;
CREATE TABLE db_flash_sale.flash_sale
(
    `id`          BIGINT       NOT NUll AUTO_INCREMENT COMMENT '商品库存ID',
    `name`        VARCHAR(120) NOT NULL COMMENT '商品名称',
    `quantity`    int          NOT NULL COMMENT '库存数量',
    `version`     int          NOT NULL DEFAULT 0 COMMENT '数据版本',
    `start_time`  TIMESTAMP    NOT NULL COMMENT '秒杀开始时间',
    `end_time`    TIMESTAMP    NOT NULL COMMENT '秒杀结束时间',
    `create_time` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    key idx_start_time (start_time),
    key idx_end_time (end_time),
    key idx_create_time (create_time)
) ENGINE = INNODB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8mb4 COMMENT ='秒杀库存表';

-- 初始化数据
# TRUNCATE table db_flash_sale.flash_sale;
INSERT into db_flash_sale.flash_sale(name, quantity, start_time, end_time)
VALUES ('1000元秒杀iphone15', 100, '2023-09-16 10:00:00', '2023-09-17 10:00:00'),
       ('800元秒杀ipad', 200, '2023-09-13 10:00:00', '2023-09-14 10:00:00'),
       ('6600元秒杀mac book pro', 300, '2023-09-13 11:00:00', '2023-09-14 10:00:00'),
       ('7000元秒杀iMac', 400, '2023-09-13 12:00:00', '2023-09-14 10:00:00');

CREATE TABLE success_sale
(
    `id`            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `flash_sale_id` BIGINT      NOT NULL COMMENT '秒杀商品ID',
    `user_phone`    VARCHAR(20) NOT NULL COMMENT '用户手机号',
    `state`         TINYINT     NOT NULL DEFAULT -1 COMMENT '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
    `create_time`   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    unique (flash_sale_id, user_phone),
    KEY idx_create_time (create_time)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT ='秒杀成功明细表';