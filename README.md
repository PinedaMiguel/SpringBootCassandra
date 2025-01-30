# Spring Boot Cassandra Project

## Project Overview
This is a Spring Boot application that integrates with Apache Cassandra database to manage product data. The application provides RESTful endpoints for creating products and retrieving the latest products.

## Technology Stack
- Spring Boot 3.1.5
- Apache Cassandra
- Spring Data Cassandra
- Docker & Docker Compose
- Java 17
- Maven
- Lombok

## Project Structure
```
src/main/java/com/project/cassandra/
├── CassandraApplication.java
├── config/
│   └── CassandraConfig.java
├── controller/
│   └── ProductController.java
├── entity/
│   └── Product.java
├── repository/
│   └── ProductRepository.java
└── service/
    └── ProductService.java
```

## Implementation Details

### 1. Cassandra Database Setup
The database is configured using Docker Compose:
```yaml
version: '3.8'
services:
  cassandra:
    image: cassandra:latest
    container_name: cassandra-db
    ports:
      - "9042:9042"
    environment:
      - CASSANDRA_CLUSTER_NAME=my-cluster
      - CASSANDRA_DC=datacenter1
      - CASSANDRA_ENDPOINT_SNITCH=SimpleSnitch
    volumes:
      - cassandra_data:/var/lib/cassandra
```

### 2. Application Configuration
Application properties (application.properties):
```properties
spring.cassandra.keyspace-name=product_keyspace
spring.cassandra.contact-points=localhost
spring.cassandra.port=9042
spring.cassandra.local-datacenter=datacenter1
spring.cassandra.schema-action=CREATE_IF_NOT_EXISTS
spring.cassandra.request.timeout=10s
spring.cassandra.connection.connect-timeout=10s
spring.cassandra.connection.init-query-timeout=10s
```

### 3. Data Model
Product entity with Cassandra mapping:
```java
@Table("products")
@Data
public class Product {
    @PrimaryKey
    private UUID id;

    @Column
    private String name;

    @Column
    private Double price;

    @Column
    private Integer quantity;

    @Column("created_at")
    private Instant createdAt;
}
```

### 4. Repository Layer
Product repository with custom query:
```java
@Repository
public interface ProductRepository extends CassandraRepository<Product, UUID> {
    @Query("SELECT * FROM products ORDER BY created_at DESC LIMIT 3")
    List<Product> findTop3ByOrderByCreatedAtDesc();
}
```

### 5. Service Layer
Service implementation:
```java
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getTop3Products() {
        return productRepository.findTop3ByOrderByCreatedAtDesc();
    }
}
```

### 6. REST Controller
API endpoints:
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){}

    @GetMapping("/latest")
    public ResponseEntity<List<Product>> getLatestProducts(){}
}
```

## Setup Instructions

### 1. Prerequisites
- Java 17
- Docker and Docker Compose
- Maven

### 2. Starting the Application

1. Start Cassandra:
```bash
docker-compose up -d
```

2. Build the application:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

## API Documentation

### 1. Create Product
**Endpoint:** POST /api/products

**Request Body:**
```json
{
    "name": "Example Product",
    "price": 99.99,
    "quantity": 10
}
```

### 2. Get Latest Products
**Endpoint:** GET /api/products/latest

**Response:**
```json
[
    {
        "id": "uuid-value",
        "name": "Example Product",
        "price": 99.99,
        "quantity": 10,
        "createdAt": "2024-01-23T10:15:30Z"
    }
]
```


## Troubleshooting

1. Verify Cassandra is running:
```bash
docker ps
```

2. Check Cassandra logs:
```bash
docker logs cassandra-db
```