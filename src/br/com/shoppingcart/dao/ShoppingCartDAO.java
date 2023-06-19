package br.com.shoppingcart.dao;

import br.com.shoppingcart.model.CartItemModel;
import br.com.shoppingcart.model.ProductModel;
import br.com.shoppingcart.model.ShoppingCartModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDAO{
    private Connection connection;

    public ShoppingCartDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO shopping_cart (product_id, quantity) VALUES ( ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            statement.setInt(2, quantity);

            statement.executeUpdate();
        }
    }

    public void delete(int productId) throws SQLException {
        String sql = "DELETE FROM shopping_cart WHERE product_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);

            statement.executeUpdate();
        }
    }

    public List<CartItemModel> getAll() throws SQLException {
        List<CartItemModel> cartProducts = new ArrayList<>();
        String sql = "SELECT c.id, p.id AS product_id, p.name, p.price, c.quantity FROM shopping_cart c " +
                "JOIN products p ON c.product_id = p.id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int cartItemId = resultSet.getInt("id");
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("name");
                double productPrice = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                ProductModel product = new ProductModel(productName, productPrice, quantity);
                CartItemModel cartItem = new CartItemModel(product, quantity);
                cartItem.setId(cartItemId);
                product.setId(productId);
                cartProducts.add(cartItem);
            }
        }

        return cartProducts;
    }

    public void clearCart() throws SQLException {
        String sql = "DELETE FROM shopping_cart";

        ShoppingCartModel shoppingCartModel = new ShoppingCartModel();
        shoppingCartModel.clearCart();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

}

