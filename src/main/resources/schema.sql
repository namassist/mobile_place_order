-- 1. Products Table
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL, -- e.g., 'Laptop', 'Book'
    price DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- INDEXING REQUIREMENT:
-- Create an index on 'type' and 'name' to optimize the Product Listing usecase[cite: 3].
-- This makes searching and filtering by category significantly faster.
CREATE INDEX IF NOT EXISTS idx_product_type ON products(type);
CREATE INDEX IF NOT EXISTS idx_product_name ON products(name);

-- 2. Orders Table (Serves as both Cart and Final Order)
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(255),
    customer_address TEXT,
    total_amount DECIMAL(19, 2) DEFAULT 0,
    status VARCHAR(20) NOT NULL, -- Values: 'DRAFT' (Cart), 'PLACED' (Saved)
    order_date TIMESTAMP
);

-- INDEXING REQUIREMENT:
-- Index on status to quickly find open carts vs history.
CREATE INDEX IF NOT EXISTS idx_order_status ON orders(status);

-- 3. Order Items Table
CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id),
    product_id BIGINT REFERENCES products(id),
    product_name VARCHAR(255), -- Snapshot of name at time of purchase
    product_type VARCHAR(100),
    price DECIMAL(19, 2), -- Snapshot of price at time of purchase
    quantity INT NOT NULL,
    subtotal DECIMAL(19, 2) NOT NULL
);

-- INDEXING REQUIREMENT:
-- Foreign key index for fast join performance when retrieving the full cart.
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);