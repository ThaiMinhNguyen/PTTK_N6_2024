CREATE DATABASE BookstoreDB;
USE BookstoreDB;

-- Bảng Address
CREATE TABLE Address (
    id INT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    zipCode VARCHAR(20) NOT NULL
);

-- Bảng Customer
CREATE TABLE Customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    address_id INT,
    FOREIGN KEY (address_id) REFERENCES Address(id)
);

-- Bảng Account
CREATE TABLE Account (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

-- Bảng Book
CREATE TABLE Book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL
);

-- Bảng Order
CREATE TABLE Orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderDate DATE NOT NULL,
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

-- Bảng OrderList 
CREATE TABLE OrderList (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    book_id INT,
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (book_id) REFERENCES Book(id)
);

INSERT INTO Address (address, zipCode) VALUES ('123 Main St', '10001');
INSERT INTO Address (address, zipCode) VALUES ('456 Elm St', '10002');

INSERT INTO Customer (name, phone, address_id) VALUES ('John Doe', '123-456-7890', 1);
INSERT INTO Customer (name, phone, address_id) VALUES ('Jane Smith', '098-765-4321', 2);

INSERT INTO Account (username, password, customer_id) VALUES ('johndoe', 'password123', 1);
INSERT INTO Account (username, password, customer_id) VALUES ('janesmith', 'password456', 2);

INSERT INTO Book (title, author, price) VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', 10.99);
INSERT INTO Book (title, author, price) VALUES ('1984', 'George Orwell', 8.99);
INSERT INTO Book (title, author, price) VALUES ('To Kill a Mockingbird', 'Harper Lee', 12.99);

INSERT INTO Orders (orderDate, customer_id) VALUES ('2024-08-17', 1);	
INSERT INTO Orders (orderDate, customer_id) VALUES ('2024-08-18', 2);

INSERT INTO OrderList (order_id, book_id) VALUES (1, 1);  
INSERT INTO OrderList (order_id, book_id) VALUES (1, 2); 
INSERT INTO OrderList (order_id, book_id) VALUES (2, 3);  





