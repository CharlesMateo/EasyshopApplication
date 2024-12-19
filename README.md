# EasyShopApplication

## Overview
EasyShopApplication is a Spring Boot-based e-commerce platform designed to simplify online shopping. The application features robust user authentication, product management, and shopping cart functionality. It integrates with Microsoft SQL Server and MySQL for data persistence and uses JSON Web Tokens (JWT) for secure authentication.

---

## Features

### Authentication & Security
- JWT-based authentication for secure user sessions.
- Role-based access control with support for `USER` and `ADMIN` roles.
- Custom security handlers for unauthorized and access-denied requests.

### User Management
- User registration with validation for roles and credentials.
- Profile management, including contact and address details.

### Product Catalog
- Add, update, and delete products (Admin only).
- Search products by category, price range, and color.
- View products grouped by categories.

### Shopping Cart
- Add, update, and remove items in the shopping cart.
- Calculate cart totals, including discounts.
- Clear cart functionality.

---

## Prerequisites
- **Java**: Version 17 or higher.
- **Maven**: Version 3.8.1 or higher.
- **Database**: Microsoft SQL Server or MySQL.

---

## Setup & Installation

### Clone the Repository
```bash
git clone https://github.com/your-repo/easyshop-application.git
cd easyshop-application
```

### Configure the Database
Update the `application.properties` file with your database credentials:

```properties
# Database configuration
spring.datasource.url=jdbc:sqlserver://<your-server>:1433;database=<your-database>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>

# JWT configuration
jwt.secret=<your-secret>
jwt.token-timeout-seconds=108000
```

### Build the Application
Run the following Maven command to build the project:
```bash
mvn clean install
```

### Run the Application
Start the Spring Boot application:
```bash
mvn spring-boot:run
```

The application will be accessible at `http://localhost:8080`.

---

## Endpoints

### Authentication
- **POST** `/login`: Authenticate a user and receive a JWT token.
- **POST** `/register`: Register a new user.

### Products
- **GET** `/products`: List all products with optional filters.
- **GET** `/products/{id}`: Retrieve a product by ID.
- **POST** `/products`: Add a new product (Admin only).
- **PUT** `/products/{id}`: Update a product (Admin only).
- **DELETE** `/products/{id}`: Delete a product (Admin only).

### Categories
- **GET** `/categories`: List all categories.
- **GET** `/categories/{id}`: Retrieve a category by ID.
- **POST** `/categories`: Add a new category (Admin only).
- **PUT** `/categories/{id}`: Update a category (Admin only).
- **DELETE** `/categories/{id}`: Delete a category (Admin only).

### Shopping Cart
- **GET** `/cart`: View the shopping cart for the logged-in user.
- **POST** `/cart/products/{productId}`: Add a product to the cart.
- **PUT** `/cart/products/{productId}`: Update product quantity in the cart.
- **DELETE** `/cart`: Clear the shopping cart.

---

## Technologies Used

### Backend
- **Spring Boot**: Web framework.
- **Spring Security**: Authentication and authorization.
- **JWT**: Token-based authentication.
- **MyBatis**: Database ORM.

### Database
- **Microsoft SQL Server**: Primary database.
- **MySQL**: Alternative database support.

### Tools
- **Maven**: Build and dependency management.
- **Apache Commons DBCP2**: Database connection pooling.

---

## Testing
Run unit tests using Maven:
```bash
mvn test
```
Test coverage includes controllers, services, and DAO layers.

---

## Deployment

### Build the JAR File
```bash
mvn package
```
The JAR file will be located in the `target` directory.

### Deploy
Deploy the generated JAR file to your preferred server environment:
```bash
java -jar target/easyshop-capstone-starter-0.0.1-SNAPSHOT.jar
```

---

## Contribution
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a feature branch.
3. Commit your changes and push the branch.
4. Submit a pull request.

---
