package com.pluralsight.NorthwindTradersAPI;

import com.pluralsight.NorthwindTradersAPI.dao.ProductDao;
import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ProductApp implements CommandLineRunner {

    private final ProductDao productDao;

    public ProductApp(ProductDao productDao) {
        this.productDao = productDao;
    }

    // Run the program and give prompts for the user.
    @Override
    public void run(String... args) {
        Scanner userInput = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = userInput.nextLine();

            switch (choice) {
                case "1":
                    listProducts();
                    break;
                case "2":
                    addProduct(userInput);
                    break;
                case "3":
                    deleteProduct(userInput);
                    break;
                case "4":
                    updateProduct(userInput);
                    break;
                case "5":
                    searchProducts(userInput);
                    break;
                case "6":
                    listAllCategories();
                    break;
                case "7":
                    searchProductsByCategory(userInput);
                    break;

                case "0":
                    exit = true;
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        userInput.close();
    }

    private void printMenu() {
        System.out.println("\n=== Product Menu ===");
        System.out.println("1. List all products");
        System.out.println("2. Add a new product");
        System.out.println("3. Delete a product");
        System.out.println("4. Update a product");
        System.out.println("5. Search products by keyword");
        System.out.println("6. List all category names");
        System.out.println("7. Search products by category name");
        System.out.println("0. Exit");
    }



    // create methods for the prompts


    // METHOD FOR LISTING PRODUCTS
    private void listProducts() {
        List<Product> products = productDao.getAll();
        for (Product product : products) {
            System.out.printf("ID: %d | Name: %s | Price: $%.2f\n",
                    product.getProductId(), product.getName(), product.getPrice());
        }
    }



    // METHOD FOR ADDING PRODUCTS
    private void addProduct(Scanner scanner) {
        System.out.println("Type '0' at any time to go back.");

        System.out.print("Enter Product ID: ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("0")) return;
        int productId = Integer.parseInt(input);

        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        if (name.equalsIgnoreCase("0")) return;

        System.out.print("Enter Unit Price: ");
        input = scanner.nextLine();
        if (input.equalsIgnoreCase("0")) return;
        double price = Double.parseDouble(input);

        System.out.print("Enter Category ID: ");
        input = scanner.nextLine();
        if (input.equalsIgnoreCase("0")) return;
        int categoryId = Integer.parseInt(input);

        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setPrice(price);
        product.setCategory(categoryId);

        productDao.add(product);
        System.out.println("Product added.");
    }



    // METHOD FOR DELETING PRODUCTS
    private void deleteProduct(Scanner scanner) {
        System.out.print("Enter Product ID to delete: ");
        if (scanner.equals("0")) return;
        int productId = Integer.parseInt(scanner.nextLine());

        if (productDao.isProductUsedInOrders(productId)) {
            System.out.println("Cannot delete product: it is used in existing orders.");
            return;
        }

        productDao.delete(productId);
        System.out.println("Product deleted.");
    }



    // METHOD FOR UPDATING PRODUCTS
    private void updateProduct(Scanner scanner) {
        System.out.println("Type '0' at any time to go back.");

        System.out.print("Enter Product ID to update: ");
        String input = scanner.nextLine();
        if (input.equals("0")) return;
        int productId = Integer.parseInt(input);

        System.out.print("Enter new Product Name: ");
        input = scanner.nextLine();
        if (input.equals("0")) return;
        String name = input;

        System.out.print("Enter new Unit Price: ");
        input = scanner.nextLine();
        if (input.equals("0")) return;
        double price = Double.parseDouble(input);

        System.out.print("Enter new Category ID: ");
        input = scanner.nextLine();
        if (input.equals("0")) return;
        int categoryId = Integer.parseInt(input);

        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setPrice(price);
        product.setCategory(categoryId);

        productDao.update(product);
        System.out.println("Product updated.");
    }




    // METHOD FOR SEARCHING FOR PRODUCTS
    private void searchProducts(Scanner scanner) {
        System.out.print("Enter keyword to search by name: ");
        String keyword = scanner.nextLine();
        if (keyword.equals("0")) return;

        List<Product> results = productDao.searchByKeyword(keyword);

        if (results.isEmpty()) {
            System.out.println("No products found.");
        } else {
            for (Product product : results) {
                System.out.printf("ID: %d | Name: %s | Price: $%.2f\n",
                        product.getProductId(), product.getName(), product.getPrice());
            }
        }
    }



    // METHOD FOR LISTING CATEGORIES
    private void listAllCategories() {
        List<String> categoryNames = productDao.getAllCategoryNames();
        System.out.println("\n=== Categories ===");
        for (String name : categoryNames) {
            System.out.println("- " + name);
        }
    }



    // METHOD FOR SEARCHING FOR PRODUCTS BY CATEGORY NAME
    private void searchProductsByCategory(Scanner scanner) {
        System.out.print("Enter category name (or type '0'): ");
        String categoryName = scanner.nextLine();
        if (categoryName.equalsIgnoreCase("0")) return;

        List<Product> results = productDao.searchByCategoryName(categoryName);
        if (results.isEmpty()) {
            System.out.println("No products found for that category.");
        } else {
            for (Product product : results) {
                System.out.printf("ID: %d | Name: %s | Price: $%.2f\n",
                        product.getProductId(), product.getName(), product.getPrice());
            }
        }
    }


}