package eos.core;
import java.util.ArrayList;

public class Cart {
    private ArrayList<Product> items;

    public Cart () {
	items = new ArrayList<>();
    }

    public void addItem (Product product) {
	items.add(product);
    }

    public ArrayList<Product> getItems () {
	return items;
    }

    public double getTotal () {
	double total = 0;

	for (Product p : items) {
	    total += p.getPrice();
	}

	return total;
    }
}
