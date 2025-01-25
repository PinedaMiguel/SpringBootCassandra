CREATE TABLE IF NOT EXISTS products
(
    bucket     text,
    created_at timestamp,
    id         uuid,
    name       text,
    price      double,
    quantity   int,
    PRIMARY KEY ((bucket), created_at, id)
) WITH CLUSTERING ORDER BY (created_at DESC, id ASC);