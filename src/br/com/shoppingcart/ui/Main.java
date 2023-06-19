package br.com.shoppingcart.ui;

import br.com.shoppingcart.dao.ProductDAO;
import br.com.shoppingcart.dao.ShoppingCartDAO;
import br.com.shoppingcart.model.CartItemModel;
import br.com.shoppingcart.model.ProductModel;
import br.com.shoppingcart.model.ShoppingCartModel;
import br.com.shoppingcart.util.ConnectionFactory;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            ProductDAO productDAO = new ProductDAO(connection);
            ShoppingCartDAO cartDAO = new ShoppingCartDAO(connection);
            Scanner scanner = new Scanner(System.in);
            int option = 0;

            System.out.println("Welcome to UalShop!");
            do{
                try{
                    System.out.println("What do you wish to do?");
                    System.out.println("1-See all products");
                    System.out.println("2-Add, remove or update a product");
                    System.out.println("3-Go to cart");
                    System.out.println("0-Leave");
                    option = scanner.nextInt();
                }catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid option.");
                    scanner.nextLine();
                }
                switch (option){
                    case 1:
                        listProducts(productDAO);
                        break;
                    case 2:
                        handleProducts(productDAO, scanner);
                        break;
                    case 3:
                        handleCart(scanner, productDAO, cartDAO);
                        break;
                    case 0:
                        System.out.println("Goodbye, we hope to see you soon!");
                        break;
                    default:
                        System.out.println("Invalid option. Please, make sure you're typing a valid option and try again.");
                        break;
                }
            }while (option != 0);
        }catch (SQLException e) {
            System.out.println("An error occurred while connecting to the database: " + e.getMessage());
        }
    }

    private static void listProducts(ProductDAO productDAO) {
        try {
            List<ProductModel> products = productDAO.getAll();
            System.out.println("Products:");
            for (ProductModel product : products) {
                System.out.println(product.getId() + " - " + product.getName() + " - " + product.getPrice() + " - " + product.getQuantityAvailable());
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving the products: " + e.getMessage());
        }
    }
    private static void handleProducts(ProductDAO productDAO, Scanner scanner) {
        int option;
        do {
            System.out.println("1-Add new product");
            System.out.println("2-Update product");
            System.out.println("3-Delete product");
            System.out.println("4-Back to menu");

            try {
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        try {
                            System.out.println("Enter product details:");
                            System.out.print("Name: ");
                            String name = scanner.nextLine();
                            System.out.print("Price: ");
                            double price = scanner.nextDouble();
                            System.out.print("Quantity: ");
                            int quantity = scanner.nextInt();

                            ProductModel newProduct = new ProductModel(name, price, quantity);
                            productDAO.insert(newProduct);
                            System.out.println("Product added successfully!");
                        } catch (SQLException e) {
                            System.out.println("An error occurred while adding the product: " + e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            System.out.print("Enter the ID of the product to update: ");
                            int productId = scanner.nextInt();
                            scanner.nextLine();

                            ProductModel updateProduct = productDAO.getProductById(productId);
                            if (updateProduct != null) {
                                System.out.println("Enter updated details for the product:");
                                System.out.print("Name: ");
                                String updatedName = scanner.nextLine();
                                System.out.print("Price: ");
                                double updatedPrice = scanner.nextDouble();
                                System.out.print("Quantity: ");
                                int updatedQuantity = scanner.nextInt();

                                updateProduct.setName(updatedName);
                                updateProduct.setPrice(updatedPrice);
                                updateProduct.setQuantityAvailable(updatedQuantity);

                                productDAO.update(updateProduct);
                                System.out.println("Product updated successfully!");
                            } else {
                                System.out.println("Product not found!");
                            }
                        } catch (SQLException e) {
                            System.out.println("An error occurred while updating the product: " + e.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            System.out.print("Enter the ID of the product to delete: ");
                            int deleteProductId = scanner.nextInt();
                            scanner.nextLine();

                            productDAO.delete(deleteProductId);
                            System.out.println("Product deleted successfully!");
                        } catch (SQLException e) {
                            System.out.println("An error occurred while deleting the product: " + e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid option.");
                scanner.nextLine(); // Limpa o buffer do scanner
                option = -1; // Define uma opção inválida para repetir o loop
            }
        } while (option != 4);
    }
    private static void handleCart(Scanner scanner, ProductDAO productDAO, ShoppingCartDAO cartDAO) {
        ShoppingCartModel shoppingCart = new ShoppingCartModel();
        int option = -1;
        int productId;
        int quantity;

        while (option != 0) {
            System.out.println("1-Add item to cart");
            System.out.println("2-Remove item from cart");
            System.out.println("3-See cart items");
            System.out.println("4-Finalize purchase");
            System.out.println("0-Back to menu");

            try {
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.print("Enter the ID of the product to add: ");
                        productId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter the quantity to add: ");
                        quantity = scanner.nextInt();
                        scanner.nextLine();

                        ProductModel productToAdd = productDAO.getProductById(productId);
                        if (productToAdd != null) {
                            if (productToAdd.getQuantityAvailable() >= quantity) {
                                shoppingCart.addProduct(productToAdd, quantity);
                                productToAdd.setQuantityAvailable(productToAdd.getQuantityAvailable() - quantity);
                                productDAO.update(productToAdd);
                                cartDAO.insert(productId, quantity);
                                System.out.println("Product added to cart successfully!");
                            } else {
                                System.out.println("Insufficient quantity available for the product!");
                            }
                        } else {
                            System.out.println("Product not found!");
                        }
                        break;
                    case 2:
                        System.out.print("Enter the ID of the product to remove: ");
                        productId = scanner.nextInt();
                        scanner.nextLine();

                        CartItemModel cartItemToRemove = null;
                        List<CartItemModel> items = cartDAO.getAll();
                        for (CartItemModel cartItem : items) {
                            if (cartItem.getProduct().getId() == productId) {
                                cartItemToRemove = cartItem;
                                break;
                            }
                        }

                        if (cartItemToRemove != null) {
                            cartDAO.delete(productId);
                            ProductModel product = cartItemToRemove.getProduct();
                            product.setQuantityAvailable(product.getQuantityAvailable() + cartItemToRemove.getQuantity());
                            productDAO.update(product);
                            shoppingCart.removeProduct(product);
                            System.out.println("Product removed from cart successfully!");
                        } else {
                            System.out.println("Product not found in the cart!");
                        }
                        break;
                    case 3:
                        List<CartItemModel> cartItems = cartDAO.getAll();
                        System.out.println("Cart items:");
                        for (CartItemModel cartItem : cartItems) {
                            ProductModel product = cartItem.getProduct();
                            System.out.println("Id: " + product.getId() + ", Product: " + product.getName() + ", Price: " + product.getPrice() + ", Quantity: " + cartItem.getQuantity());
                        }
                        ShoppingCartModel cartPrice = new ShoppingCartModel();
                        double total = cartPrice.calculatePrice(cartDAO, productDAO);
                        System.out.println("Total value: " + total);
                        break;
                    case 4:
                        ShoppingCartModel shoppingCartPrice = new ShoppingCartModel();
                        double totalValue = shoppingCartPrice.calculatePrice(cartDAO, productDAO);
                        System.out.println("Are you sure you want to finalize the purchase?");
                        System.out.println("Total value: " + totalValue);
                        System.out.println("1- Yes, proceed.");
                        System.out.println("2- No, back to cart.");
                        int confirmation = scanner.nextInt();
                        if (confirmation == 1) {
                            cartDAO.clearCart();
                            System.out.println("Purchase finalized successfully!");
                        } else {
                            System.out.println("Purchase canceled.");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option. Please, make sure you're typing a valid option and try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.nextLine();
            } catch (SQLException e) {
                System.out.println("Sorry, something went wrong while trying to access the database.");
            }
        }
    }
}