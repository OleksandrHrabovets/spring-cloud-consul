create table if not exists order_item
(
    id         int primary key,
    amount     int,
    id_product int
);