CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        order_number VARCHAR(100) NOT NULL UNIQUE,
                        user_id BIGINT NOT NULL,
                        product_id BIGINT NOT NULL,
                        quantity INT NOT NULL,
                        total_price NUMERIC(10, 2) NOT NULL,
                        status VARCHAR(50) NOT NULL DEFAULT 'PLACED',
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id),
                        CONSTRAINT fk_order_product FOREIGN KEY (product_id) REFERENCES products(id)
);