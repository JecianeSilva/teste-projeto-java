CREATE TABLE EMPLOYEE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_birth DATE NOT NULL,
    salary DECIMAL(15, 2) NOT NULL,
    role VARCHAR(255) NOT NULL
);
