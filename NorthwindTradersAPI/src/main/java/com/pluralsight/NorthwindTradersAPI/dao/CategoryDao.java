package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.models.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> getAllCategories();
    Category getCategoryById(int categoryId);
    Category addNewCategory(Category category);
    void update (int id, Category category);

}
