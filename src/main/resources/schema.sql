CREATE TABLE IF NOT EXISTS customer (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    account_balance DECIMAL(15,2),
    status VARCHAR(20),
    processed_by VARCHAR(50)
);

CREATE INDEX idx_customer_email ON customer(email);
CREATE INDEX idx_customer_status ON customer(status);