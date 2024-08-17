create database Bookstore;
USE Bookstore;
 
CREATE TABLE `book` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) NOT NULL,
  `author` varchar(45) NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `book_id_UNIQUE` (`book_id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;


CREATE TABLE `accounts` (
  `username` varchar(45) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into accounts (username, password) values
	('minh', 'minh');

INSERT INTO book (title, author, price) VALUES
('To Kill a Mockingbird', 'Harper Lee', 18.99),
('1984', 'George Orwell', 15.99),
('Pride and Prejudice', 'Jane Austen', 12.50),
('The Great Gatsby', 'F. Scott Fitzgerald', 14.75),
('Moby Dick', 'Herman Melville', 19.99);