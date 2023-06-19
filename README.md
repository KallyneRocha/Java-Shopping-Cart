# Java-Shopping-Cart
This is a Java project that implements a shopping cart system for a store. The project uses JDBC to interact with a PostgreSQL database.

## Features
1. List, update, add, and remove products from the store.
2. List, add and remove products from the shopping cart.
3. Display the total value of items in the cart.
4. Option to finish the purchase with user confirmation.

## Project Setup
1. Clone this repository to your environment.
2. Make sure you have Java JDK (version 17 or higher) installed.
3. Import the project into your favorite IDE.
4. Create a PostgreSQL database and configure the connection in the "ConnectionFactory.java" file, inside the "util" folder.
5. Execute the "initialize_database.sql" file in pgAdmin to create the necessary tables and insert the example products.
6. Compile and run the project from the "Main.java" file.
