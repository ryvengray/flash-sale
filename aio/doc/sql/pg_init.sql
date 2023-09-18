-- 创建 schema
create schema fs;
create table flash_sale
(
    id          integer                 not null,
    name        varchar                 not null,
    quantity    integer                 not null,
    start_time  timestamp               not null,
    end_time    timestamp               not null,
    version     integer   default 0     not null,
    create_time timestamp default now() not null
);

comment on table flash_sale is '秒杀表';
comment on column flash_sale.id is 'ID';
comment on column flash_sale.name is '商品名称';
comment on column flash_sale.quantity is '数量';
comment on column flash_sale.start_time is '秒杀开始时间';
comment on column flash_sale.end_time is '秒杀结束时间';
comment on column flash_sale.version is '乐观锁版本';
comment on column flash_sale.create_time is '创建时间';
alter table flash_sale
    owner to postgres;
create index time_idx
    on flash_sale (start_time, end_time);
comment on index time_idx is '时间';

-- 初始化数据
INSERT into fs.flash_sale(id, name, quantity, start_time, end_time)
VALUES (1000, '1000元秒杀iphone15', 100, '2023-09-16 10:00:00', '2023-09-17 10:00:00'),
       (1001, '800元秒杀ipad', 200, '2023-09-13 10:00:00', '2023-09-14 10:00:00'),
       (1002, '6600元秒杀mac book pro', 300, '2023-09-13 11:00:00', '2023-09-14 10:00:00'),
       (1003, '7000元秒杀iMac', 400, '2023-09-13 12:00:00', '2023-09-14 10:00:00');

-- 秒杀成功表
create table success_sale
(
    id            integer generated always as identity,
    flash_sale_id integer                 not null,
    user_phone    varchar(32),
    state         integer   default 1     not null,
    create_time   timestamp default now() not null,
    constraint sale_user
        unique (flash_sale_id, user_phone)
);

comment on table success_sale is '秒杀成功记录';
comment on column success_sale.id is '主键ID';
comment on column success_sale.flash_sale_id is '秒杀商品ID';
comment on column success_sale.user_phone is '用户手机号';
comment on column success_sale.state is '状态';
comment on column success_sale.create_time is '创建时间';
alter table success_sale
    owner to postgres;