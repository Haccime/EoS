package eos.core;
import java.util.ArrayList;

public class Inventory {
    private ArrayList<Product> products;

    public Inventory () {
	products = new ArrayList<>();
	products.add(new Product("Apple", "P00", 2.00, 50));
	products.add(new Product("Orange", "P01", 1.50, 100));
    }

    public ArrayList<Product> getAll() {
	return products;
    }

}
