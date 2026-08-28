CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      order_number VARCHAR(100) NOT NULL UNIQUE,
                                      product_id BIGINT NOT NULL,
                                      username VARCHAR(100) NOT NULL,
                                      quantity INT NOT NULL,
                                      total_price NUMERIC(10, 2) NOT NULL,
                                      order_status VARCHAR(50) NOT NULL,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);