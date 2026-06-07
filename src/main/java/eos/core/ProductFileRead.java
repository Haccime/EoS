package eos.core;
import java.io.*;
import java.util.*;

public class ProductFileRead {
    private static final String FILE = "products.txt";

    public static List<Product> loadProducts() {
	List<Product> products = new ArrayList<>();
	try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
	    String line;
	    while ((line = br.readLine()) != null) {
		if (line.trim().isEmpty()) continue;
		String[] parts = line.split(",");
		products.add(new Product(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()), Integer.parseInt(parts[3].trim())));
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return products;
    }

    public static void saveProducts(List<Product> products) {
	try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
	    for (Product p : products) {
		bw.write(p.getName() + "," + p.getCode() + "," + p.getPrice() + "," + p.getAvailability());
		bw.newLine();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
