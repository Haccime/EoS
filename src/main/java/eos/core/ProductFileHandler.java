package eos.core;

import java.io.*;
import java.util.*;

public class ProductFileHandler {
    private static final String FILE = "data/products.txt";

    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        File file = new File(FILE);
        
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return products;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                
                // Consistency Check: Ensure parts[0] is Name and parts[1] is Code 
                // matches your constructor: Product(String name, String code, double price, int availability)
                String name = parts[0].trim();
                String code = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                int availability = Integer.parseInt(parts[3].trim());
                
                products.add(new Product(name, code, price, availability));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static void saveProducts(List<Product> products) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Product p : products) {
                // Must match the exact order used in loadProducts()
                bw.write(p.getName() + "," + p.getCode() + "," + p.getPrice() + "," + p.getAvailability());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}