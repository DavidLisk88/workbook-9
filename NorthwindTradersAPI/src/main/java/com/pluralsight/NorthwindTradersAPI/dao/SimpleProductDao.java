package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimpleProductDao implements ProductDao {
    private final List<Product> products = new ArrayList<>();

    @Override
    public List<Product> getAll() {
        return products;
    }



/* This code looks through all the products,
finds the one with the ID number I asked for,
and gives it back to me. If it can’t find it, it gives me nothing.
 */
    @Override
    public Product getById(int productId) {
        return products.stream()
                .filter(product -> product.getProductId() == productId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void add(Product product) {
        products.add(product);
    }

    @Override
    public void update(Product product) {
        Product existing = getById(product.getProductId());
        if (existing != null) {
            existing.setName(product.getName());
            existing.setPrice(product.getPrice());
            existing.setCategory(product.getCategory());
        }
    }

    @Override
    public void delete(int productId) {
        products.removeIf(product -> product.getProductId() == productId);
    }

    @Override
    public List<Product> searchByKeyword(String keyword) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    @Override
    public void insertProductWithCategoryName(Product product, String categoryName) {

        this.add(product);
    }

    @Override
    public boolean isProductUsedInOrders(int productId) {

        return false;
    }

    @Override
    public List<String> getAllCategoryNames() {
        List<String> categoryNames = new ArrayList<>();
        for (Product product : products) {
            String name = getCategoryNameFromId(product.getCategory());
            if (!categoryNames.contains(name)) {
                categoryNames.add(name);
            }
        }
        return categoryNames;
    }



    // SEARCH CATEGORY BY NMME
    @Override
    public List<Product> searchByCategoryName(String categoryName) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (getCategoryNameFromId(product.getCategory()).equalsIgnoreCase(categoryName)) {
                results.add(product);
            }
        }
        return results;
    }



    // THIS METHOD ASSIGNS EACH CATEGORY TO ITS ID FOR THE USER
    private String getCategoryNameFromId(int categoryId) {
        switch (categoryId) {
            case 1: return "Beverages";
            case 2: return "Condiments";
            case 3: return "Confections";
            case 4: return "Dairy Products";
            case 5: return "Grains/Cereals";
            case 6: return "Meat/Poultry";
            case 7: return "Produce";
            case 8: return "Seafood";
            default: return "Unknown";
        }
    }


}
