package br.com.shoppingcart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.shoppingcart.model.ProductModel;

public class ProductDAO implements DAO<ProductModel> {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(ProductModel product) throws SQLException {
        String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantityAvailable());

            statement.executeUpdate();
        }
    }

    @Override
    public List<ProductModel> getAll() throws SQLException {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantityAvailable = resultSet.getInt("quantity");

                ProductModel product = new ProductModel(name, price, quantityAvailable);
                product.setId(id);
                products.add(product);
            }
        }

        return products;
    }

    public ProductModel getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    int quantityAvailable = resultSet.getInt("quantity");

                    ProductModel product = new ProductModel(name, price, quantityAvailable);
                    product.setId(id); // Atribuir o ID ao objeto ProductModel

                    return product;
                }
            }
        }

        return null; // Retornar null se não encontrou nenhum produto com o ID fornecido
    }



    @Override
    public void update(ProductModel product) throws SQLException {
        String sql = "UPDATE products SET name = ?, price = ?, quantity = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantityAvailable());
            statement.setInt(4, product.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No product found with ID: " + product.getId());
            }
        }
    }


    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        ShoppingCartDAO cartDAO = new ShoppingCartDAO(connection);
        cartDAO.delete(id);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }


}

