CREATE TABLE IF NOT EXISTS customers (
    customer_id     VARCHAR(20) PRIMARY KEY,
    username        VARCHAR(50) UNIQUE NOT NULL,
    password        VARCHAR(50) NOT NULL,
    customer_type   VARCHAR(10) NOT NULL,
    first_name      VARCHAR(50),
    last_name       VARCHAR(50),
    date_of_birth   DATE,
    gender          VARCHAR(10),
    id_number       VARCHAR(9),
    company_name    VARCHAR(100),
    registration_no VARCHAR(50),
    contact_person  VARCHAR(100),
    address         VARCHAR(200),
    phone_number    VARCHAR(20),
    email           VARCHAR(100),
    branch          VARCHAR(50),
    registration_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(30) PRIMARY KEY,
    customer_id    VARCHAR(20) NOT NULL,
    account_type   VARCHAR(15) NOT NULL,
    balance        DECIMAL(15,2) NOT NULL,
    branch         VARCHAR(50),
    status         VARCHAR(15) DEFAULT 'ACTIVE',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id  VARCHAR(30) PRIMARY KEY,
    account_number  VARCHAR(30) NOT NULL,
    transaction_type VARCHAR(15) NOT NULL,
    amount          DECIMAL(15,2) NOT NULL,
    description     VARCHAR(255),
    balance_after   DECIMAL(15,2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);