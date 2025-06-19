package com.pluralsight.NorthwindTradersAPI.controllers;

import com.pluralsight.NorthwindTradersAPI.config.DatabaseConfig;
import com.pluralsight.NorthwindTradersAPI.dao.JdbcCategoryDao;
import com.pluralsight.NorthwindTradersAPI.models.Category;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/categories")
public class CategoriesController {

    private JdbcCategoryDao categoryDao;

    public CategoriesController(DatabaseConfig config) throws SQLException {
        this.categoryDao = new JdbcCategoryDao(config);
    }

    @GetMapping
    public List<Category> getAllCategories(@RequestParam(required = false) String name) {
       if (categoryDao == null)
           return new ArrayList<>();

        List<Category> allCategories = categoryDao.getAllCategories();
        List<Category> result = new ArrayList<>();

        /* shuffle through all categories.
        if I don't specify which category, then return all categories
        if i specify the category, return that specified category
         */
        for (Category category : allCategories) {
            if (name == null || category.getCategoryName().equalsIgnoreCase(name)) {
                result.add(category);
            }
        }

        return result;
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable int id) {
        if (categoryDao == null)
            return new Category();

        Category category = categoryDao.getCategoryById(id);
        return category != null ? category : new Category();
    }

    @PostMapping("/add")
    public Category addNewCategory(@RequestBody Category category){
        System.out.println("Added: " + category);

        categoryDao.addNewCategory(category);
        return category;
    }

    @PutMapping("/update/{id}")
    public void update (@PathVariable int id, @RequestBody Category category){

        System.out.println("Updated: " + id + "-----" + category);
        categoryDao.update(id, category);
    }

    @DeleteMapping("/delete/{id}")
    public void delete (@PathVariable int id){
        categoryDao.delete(id);
    }


}





