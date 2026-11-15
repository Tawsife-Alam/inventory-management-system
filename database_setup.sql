-- =======================================================
-- UNIVERSITY CSE DBMS LAB PROJECT
-- INVENTORY MANAGEMENT SYSTEM - COMPLETE DATABASE SETUP
-- =======================================================

-- Step 1: Create Database
CREATE DATABASE IF NOT EXISTS inventory_management;
USE inventory_management;

-- Step 2: Drop existing tables if re-running (respecting FK order)
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS categories;

-- Step 3: Create Categories Table
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL
);

-- Step 4: Create Suppliers Table
CREATE TABLE suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL
);

-- Step 5: Create Products Table (Child table with Foreign Keys)
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(150) NOT NULL,
    category_id INT,
    supplier_id INT,
    quantity INT NOT NULL DEFAULT 0,
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_products_categories
        FOREIGN KEY (category_id) REFERENCES categories(category_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_products_suppliers
        FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- Step 6: Insert Sample Categories
INSERT INTO categories (category_name) VALUES 
('Electronics'),
('Stationery'),
('Groceries'),
('Home Appliances');

-- Step 7: Insert Sample Suppliers
INSERT INTO suppliers (supplier_name, phone) VALUES 
('TechSource Ltd.', '01711-112233'),
('Global Paper Mills', '01822-334455'),
('FreshFarm Traders', '01933-556677'),
('HomeComforts Co.', '01644-778899');

-- Step 8: Insert 10 Sample Products
INSERT INTO products (product_name, category_id, supplier_id, quantity, price) VALUES 
('Wireless Mouse', 1, 1, 45, 15.50),
('Mechanical Keyboard', 1, 1, 20, 45.00),
('USB-C Charging Cable', 1, 1, 100, 5.00),
('Spiral Notebook A4', 2, 2, 150, 2.50),
('Gel Pen (Pack of 10)', 2, 2, 80, 4.00),
('Sticky Notes Pastel', 2, 2, 60, 1.75),
('Organic Green Tea (50 bags)', 3, 3, 35, 8.20),
('Roasted Almonds (500g)', 3, 3, 25, 12.00),
('Electric Kettle 1.8L', 4, 4, 18, 28.50),
('Desk LED Lamp', 4, 4, 30, 19.99);

-- =======================================================
-- VERIFICATION QUERIES (Used in the Application)
-- =======================================================

-- 1. Read All Products with Joined Category & Supplier Names
SELECT 
    p.product_id,
    p.product_name,
    c.category_name,
    s.supplier_name,
    p.quantity,
    p.price,
    (p.quantity * p.price) AS total_value
FROM products p
LEFT JOIN categories c ON p.category_id = c.category_id
LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id
ORDER BY p.product_id ASC;

-- 2. Aggregate Query for Summary Statistics
SELECT 
    COUNT(*) AS total_products,
    COALESCE(SUM(quantity), 0) AS total_quantity,
    COALESCE(SUM(quantity * price), 0) AS total_inventory_value
FROM products;

-- 3. Search Query Example (LIKE with wildcards)
SELECT 
    p.product_id,
    p.product_name,
    c.category_name,
    s.supplier_name,
    p.quantity,
    p.price,
    (p.quantity * p.price) AS total_value
FROM products p
LEFT JOIN categories c ON p.category_id = c.category_id
LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id
WHERE p.product_name LIKE '%Mouse%'
ORDER BY p.product_id ASC;
