CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(500),
                          price NUMERIC(10, 2) NOT NULL,
                          stock_quantity INT NOT NULL DEFAULT 0,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);