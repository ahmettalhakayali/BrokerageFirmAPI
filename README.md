# BrokerageFirmAPI

BrokerageFirmAPI is a Spring Boot application that provides a RESTful API for managing brokerage operations, such as creating and managing orders, handling customer assets, depositing and withdrawing money, and listing transactions. 
The application includes essential endpoints for handling stock exchange operations.

# Technologies

- Java 8 (JDK)
- Spring Boot for rapid API development
- Maven for project build and dependency management
- H2 Database (or any database you are using)
- JUnit for unit testing
- Mockito for mocking in tests

# Prerequisites
To run this project, you need the following:

- Java 8 or later installed
- Maven installed (or use the Maven wrapper mvnw included in the project)
- An IDE (IntelliJ IDEA, Eclipse, Spring Tool Suite) or text editor (I used Spring Suite Tools 4 with Eclipse)
- Git installed
- Java 8 or later installed

# Setup
Cloning the Repository
Clone this repository to your local machine:

- git clone https://github.com/ahmettalhakayali/BrokerageFirmAPI.git

Navigate into the project directory:

- cd BrokerageFirmAPI

# Build the Project
Use Maven to resolve dependencies and build the project. You can either use the Maven wrapper included (mvnw or mvnw.cmd) or use a global Maven installation.
- ./mvnw clean install

 or
 
- mvn clean install

 # Running the Application

Run the project using Maven's Spring Boot plugin:
- ./mvnw spring-boot:run

or

- mvn spring-boot:run

The API will be available at:

- http://localhost:8080

# API Endpoints

# Order Management

- Create a new order
  
POST /orders/create

Request body example:

{

  "customerId": String,
  "assetName": String,
  "side": String,
  "size": int,
  "price": double
  
}

- List all orders
  
 POST orders/list

 Request body example:

 {
 
  "customerId": string,
  "startDate": string (Format: YYYY-MM-DD),  
  "endDate": string (Format: YYYY-MM-DD)
  "status": string (optional value)
  "side": string (optional value)
  
}

- Delete an order
  
 POST orders/delete
 
 Request body example:

{

  "orderId": long,
  "customerId": string
  
}

# Asset Managenment

- List assets
  
POST /assets/list

Request body example:

{

  "customerId": long

}

- Deposit money to an asset
  
POST /assets/deposit

Request body example:

{

  "customerId": long,
  "amount": double
  
}

- Withdraw money from an asset
  
POST /assets/withdraw

Request body example:

{

  "customerId": long,
  "amount": double,
  "iban": string
  
}

# Testing
Running Unit Tests
The project includes unit tests for the main service classes. To run the tests, execute the following command:

- ./mvnw test

or

- mvn test

# Test Libraries

- JUnit: For unit testing.
- Mockito: For mocking repository dependencies in service layer tests.

# THE END
