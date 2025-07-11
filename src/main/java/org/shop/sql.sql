DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;


-- Tworzenie tabeli User
CREATE TABLE users (
                       userid TEXT PRIMARY KEY,
                       login TEXT NOT NULL,
                       passwd TEXT NOT NULL,
                       roles TEXT NOT NULL,
                       isactive BOOLEAN NOT NULL
);

-- Tworzenie tabeli Product
-- Tworzenie tabeli Product
CREATE TABLE products (
                          productid TEXT PRIMARY KEY,
                          price NUMERIC NOT NULL,
                          title TEXT NOT NULL,
                          description TEXT,
                          category TEXT NOT NULL,
                          isactive BOOLEAN NOT NULL
);


-- Tworzenie tabeli Order
CREATE TABLE orders (
                        orderid TEXT PRIMARY KEY,
                        userId TEXT NOT NULL,
                        status TEXT NOT NULL,
                        products TEXT,
                        paymentStatus Text,
                        stripeid Text,
                        FOREIGN KEY (userId) REFERENCES users(userid)
);

-- Tworzenie tabeli Cart
CREATE TABLE carts (
                       cartid TEXT PRIMARY KEY,
                       userid TEXT NOT NULL,
                       products TEXT,
                       FOREIGN KEY (userId) REFERENCES users(userid)
);

CREATE TABLE payment (
    paymentid TEXT PRIMARY KEY,
    orderid TEXT,
    userid TEXT,
    paymentstatus TEXT

)