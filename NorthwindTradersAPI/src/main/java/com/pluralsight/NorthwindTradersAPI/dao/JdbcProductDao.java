package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.config.DatabaseConfig;
import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class JdbcProductDao implements ProductDao {
    private final Connection connection;

    public JdbcProductDao(DatabaseConfig config) throws SQLException {
        this.connection = config.getConnection();
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Product product = new Product();
                product.setProductId(results.getInt("ProductID"));
                product.setName(results.getString("ProductName"));
                product.setPrice(results.getDouble("UnitPrice"));
                product.setCategory(results.getInt("CategoryID"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product getById(int id) {
        String sql = "SELECT * FROM products WHERE ProductID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                Product product = new Product();
                product.setProductId(results.getInt("ProductID"));
                product.setName(results.getString("ProductName"));
                product.setPrice(results.getDouble("UnitPrice"));
                product.setCategory(results.getInt("CategoryID"));
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(Product product) {
        String sql = "INSERT INTO products (ProductID, ProductName, UnitPrice, CategoryID) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, product.getProductId());
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getCategory());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET ProductName = ?, UnitPrice = ?, CategoryID = ? WHERE ProductID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getCategory());
            statement.setInt(4, product.getProductId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM products WHERE ProductID = ?";


        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> searchByKeyword(String keyword) {
        List<Product> resultsList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE ProductName LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                Product product = new Product();
                product.setProductId(results.getInt("ProductID"));
                product.setName(results.getString("ProductName"));
                product.setPrice(results.getDouble("UnitPrice"));
                product.setCategory(results.getInt("CategoryID"));
                resultsList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultsList;
    }

    @Override
    public void insertProductWithCategoryName(Product product, String categoryName) {
        try {
            String categorySql = "SELECT CategoryID FROM categories WHERE CategoryName = ?";
            int categoryId = -1;

            try (PreparedStatement statement = connection.prepareStatement(categorySql)) {
                statement.setString(1, categoryName);
                ResultSet results = statement.executeQuery();
                if (results.next()) {
                    categoryId = results.getInt("CategoryID");
                } else {
                    System.out.println("Category not found.");
                    return;
                }
            }

            String insertSql = "INSERT INTO products (ProductID, ProductName, UnitPrice, CategoryID) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                statement.setInt(1, product.getProductId());
                statement.setString(2, product.getName());
                statement.setDouble(3, product.getPrice());
                statement.setInt(4, categoryId);
                statement.executeUpdate();
                System.out.println("Product inserted successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isProductUsedInOrders(int productId) {
        String sql = "SELECT COUNT(*) FROM `order details` WHERE ProductID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                int count = results.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> getAllCategoryNames() {
        List<String> categoryNames = new ArrayList<>();
        String sql = "SELECT CategoryName FROM categories";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                categoryNames.add(results.getString("CategoryName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryNames;
    }

    @Override
    public List<Product> searchByCategoryName(String categoryName) {
        List<Product> results = new ArrayList<>();
        String sql = "SELECT p.ProductID, p.ProductName, p.UnitPrice, p.CategoryID " +
                "FROM products p " +
                "JOIN categories c ON p.CategoryID = c.CategoryID " +
                "WHERE c.CategoryName = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getInt("ProductID"));
                product.setName(resultSet.getString("ProductName"));
                product.setPrice(resultSet.getDouble("UnitPrice"));
                product.setCategory(resultSet.getInt("CategoryID"));
                results.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }


}