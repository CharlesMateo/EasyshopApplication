-- ----------------------------------------------------------------------
-- Target DBMS:           Azure SQL Database
-- Project name:          EasyShop
-- ----------------------------------------------------------------------

-- Drop the database if it exists (Azure SQL Database doesn't support DROP DATABASE directly)
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'EasyShop')
BEGIN
    ALTER DATABASE EasyShop SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE EasyShop; -- Note: This statement is included for reference but will not work in Azure SQL Database.
END
GO

-- Create the database (In Azure SQL Database, databases are usually pre-created)
-- Skipping CREATE DATABASE as it may be managed at the server level on Azure.
GO

-- Use the newly created database
-- In Azure SQL Database, you are connected directly to the database, so this statement is unnecessary:
-- USE EasyShop;
GO

-- ----------------------------------------------------------------------
-- Tables
-- ----------------------------------------------------------------------

-- Drop tables if they exist in the correct order to avoid foreign key conflicts
DROP TABLE IF EXISTS order_line_items;
DROP TABLE IF EXISTS shopping_cart;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS profiles;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
GO

-- Create 'users' table
CREATE TABLE users (
    user_id INT NOT NULL IDENTITY(1,1),
    username NVARCHAR(50) NOT NULL,
    hashed_password NVARCHAR(255) NOT NULL,
    role NVARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id)
);
GO

-- Create 'profiles' table
CREATE TABLE profiles (
    user_id INT NOT NULL,
    first_name NVARCHAR(50) NOT NULL,
    last_name NVARCHAR(50) NOT NULL,
    phone NVARCHAR(20) NOT NULL,
    email NVARCHAR(200) NOT NULL,
    address NVARCHAR(200) NOT NULL,
    city NVARCHAR(50) NOT NULL,
    state NVARCHAR(50) NOT NULL,
    zip NVARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO

-- Create 'categories' table
CREATE TABLE categories (
    category_id INT NOT NULL IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    PRIMARY KEY (category_id)
);
GO

-- Create 'products' table
CREATE TABLE products (
    product_id INT NOT NULL IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category_id INT NOT NULL,
    description NVARCHAR(MAX),
    color NVARCHAR(20),
    image_url NVARCHAR(200),
    stock INT NOT NULL DEFAULT 0,
    featured BIT NOT NULL DEFAULT 0,
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
GO

-- Create 'orders' table
CREATE TABLE orders (
    order_id INT NOT NULL IDENTITY(1,1),
    user_id INT NOT NULL,
    date DATETIME NOT NULL,
    address NVARCHAR(100) NOT NULL,
    city NVARCHAR(50) NOT NULL,
    state NVARCHAR(50) NOT NULL,
    zip NVARCHAR(20) NOT NULL,
    shipping_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (order_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO

-- Create 'order_line_items' table
CREATE TABLE order_line_items (
    order_line_item_id INT NOT NULL IDENTITY(1,1),
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    sales_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    discount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (order_line_item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
GO

-- Create 'shopping_cart' table
CREATE TABLE shopping_cart (
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
GO

-- ----------------------------------------------------------------------
-- Insert Data
-- ----------------------------------------------------------------------

-- Insert Users
INSERT INTO users (username, hashed_password, role)
VALUES
    ('user', '$2a$10$NkufUPF3V8dEPSZeo1fzHe9ScBu.LOay9S3N32M84yuUM2OJYEJ/.', 'ROLE_USER'),
    ('admin', '$2a$10$lfQi9jSfhZZhfS6/Kyzv3u3418IgnWXWDQDk7IbcwlCFPgxg9Iud2', 'ROLE_ADMIN'),
    ('george', '$2a$10$lfQi9jSfhZZhfS6/Kyzv3u3418IgnWXWDQDk7IbcwlCFPgxg9Iud2', 'ROLE_USER');
GO

-- Insert Profiles
INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip)
VALUES
    (1, 'Joe', 'Joesephus', '800-555-1234', 'joejoesephus@email.com', '789 Oak Avenue', 'Dallas', 'TX', '75051'),
    (2, 'Adam', 'Admamson', '800-555-1212', 'aaadamson@email.com', '456 Elm Street','Dallas','TX','75052'),
    (3, 'George', 'Jetson', '800-555-9876', 'george.jetson@email.com', '123 Birch Parkway','Dallas','TX','75051');
GO

-- Insert Categories
INSERT INTO categories (name, description)
VALUES
    ('Electronics', 'Explore the latest gadgets and electronic devices.'),
    ('Fashion', 'Discover trendy clothing and accessories for men and women.'),
    ('Home & Kitchen', 'Find everything you need to decorate and equip your home.');
GO
