package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.config.DatabaseConfig;
import com.pluralsight.NorthwindTradersAPI.models.Category;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCategoryDao implements CategoryDao {

    private final Connection connection;

    public JdbcCategoryDao(DatabaseConfig config) throws SQLException {
        this.connection = config.getConnection();
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Category category = new Category();
                category.setCategoryId(results.getInt("CategoryID"));
                category.setCategoryName(results.getString("CategoryName"));
                categories.add(category);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;


    }

    @Override
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE CategoryID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Category category = new Category();
                category.setCategoryId(result.getInt("CategoryID"));
                category.setCategoryName(result.getString("CategoryName"));
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category addNewCategory(Category category) {
        String sql = "INSERT INTO categories (CategoryID, CategoryName) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, category.getCategoryId());
            statement.setString(2, category.getCategoryName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;

    }

    @Override
    public void update (int id, Category category){
        String sql = "UPDATE categories SET CategoryName = ? WHERE CategoryID = ?;";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, category.getCategoryName());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}

