# Billing-Service

This Spring Boot application calculates the net payable amount for a bill at a retail store, 
applying various discount rules based on user type and purchase details.

# Features

- Discounts Application:
  -  Employee Discount: 30% discount for store employees.
  -  Affiliate Discount: 10% discount for store affiliates.
  -  Loyal Customer Discount: 5% discount for customers who have been with the store for over 2 years.
  -  Bulk Purchase Discount: $5 discount for every $100 spent.
  -  Exclusions: Percentage-based discounts do not apply to groceries.
  -  Single Discount Policy: Only one percentage-based discount can be applied per bill.

# Setup Instructions 
  - Java 17 
  - Maven 3.8.3
  - Spring Boot 3.X

# Installation
    - Clone the repository - git clone https://github.com/sargam-mishra/retail-store-billing.git
    - Navigate to the project directory - cd retail-store-billing

# Build and Run
    - Build the project - mvn clean install
    - Run the application - mvn spring-boot:run

# API Endpoints
    - Calculate Net Payable Amount: POST /discount/calculate
    - Request Body: 
    ```
    {
        "customerId": 1,
        "products": [
            {
                "id": 1,
                "name": "Product 1",
                "price": 100,
                "category": "GROCERY"
            },
            {
                "id": 2,
                "name": "Product 2",
                "price": 100,
                "category": "NON_GROCERY"
            }        
        }




# Testing

The application includes unit tests for the discount calculation logic. 
Test coverage includes:
   - Discounts Application: Testing various scenarios for employee, affiliate, and loyal customer discounts.
   - Bulk Purchase Discount: Verifying the discount for amounts exceeding $100.
   - Discount Exclusions: Ensuring percentage-based discounts are not applied to groceries.
   - Single Discount Rule: Validating that only one percentage-based discount is applied per bill.

# Run Tests
    - Run the tests - mvn test
