package com.project.cassandra.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("products")
@Data
public class Product
{
    @PrimaryKeyColumn(name = "bucket", type = PrimaryKeyType.PARTITIONED)
    private String bucket = "recent";

    @PrimaryKeyColumn(name = "created_at", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Instant createdAt;

    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED)
    private UUID id;

    @Column
    private String name;

    @Column
    private Double price;

    @Column
    private Integer quantity;


    public Product()
    {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.bucket = "recent";
    }
}
