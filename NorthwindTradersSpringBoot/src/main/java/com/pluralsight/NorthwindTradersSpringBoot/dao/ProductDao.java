package com.pluralsight.NorthwindTradersSpringBoot.dao;

import com.pluralsight.NorthwindTradersSpringBoot.models.Product;
import java.util.List;

public interface ProductDao {
    List<Product> getAll();
    Product getById(int productId);
    void add(Product product);
    void update(Product product);
    void delete(int productId);
    List<Product> searchByKeyword(String keyword);
    void insertProductWithCategoryName(Product product, String categoryName);
    boolean isProductUsedInOrders(int productId);
    List<String> getAllCategoryNames();
    List<Product> searchByCategoryName(String categoryName);

}