package com.pluralsight.NorthwindTradersAPI.controllers;


import com.pluralsight.NorthwindTradersAPI.config.DatabaseConfig;
import com.pluralsight.NorthwindTradersAPI.dao.JdbcCategoryDao;
import com.pluralsight.NorthwindTradersAPI.dao.JdbcProductDao;
import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private JdbcProductDao productDao;

    public ProductsController(DatabaseConfig config) throws SQLException {
        this.productDao = new JdbcProductDao(config);
    }

    @GetMapping
    public List<Product> getAllProducts(
            @RequestParam(required = false)
            String name,
            @RequestParam(required = false)
            Integer categoryId,
            @RequestParam(required = false)
            Double price) {

        List<Product> allProducts = productDao.getAll();
        List<Product> filtered = new ArrayList<>();

        for (Product product : allProducts) {
            boolean matches = true;
            if (name != null && !product.getName().equalsIgnoreCase(name)) {
                matches = false;
            }
            if (categoryId != null && !categoryId.equals(product.getCategory())) {
                matches = false;
            }
            if (price != null && product.getPrice() != price.doubleValue()) {
                matches = false;
            }
            if (matches) {
                filtered.add(product);
            }
        }

        return filtered;
    }


    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        Product product = productDao.getById(id);
        return product != null ? product : new Product();
    }

    @PostMapping("/add")
    public Product add(@RequestBody Product product) {
        productDao.add(product);
        System.out.println("Added: " + product);
        return product;
    }

    @PutMapping("/update/{id}")
    public void update (@PathVariable int id, @RequestBody Product product){
        productDao.update(id, product);
        System.out.println("Product Updated: " + product);

    }

}



