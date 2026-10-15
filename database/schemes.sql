CREATE DATABASE web_service_basic;

USE web_service_basic;

CREATE TABLE students(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    province_id INT
);