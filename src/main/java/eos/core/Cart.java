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

    public void removeItem(String code, int quantity) {
	for (int i = 0; i < quantity; i++) {
	    for (int j = 0; j < items.size(); j++) {
		if (items.get(j).getCode().equals(code)) {
		    items.remove(j);
		    break;
		}
	    }
	}
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

    public void checkout ( Inventory inventory, String username) {
	for ( Product p : items ) {
	    inventory.reduceStock(p.getCode(), 1);
	}
	items.clear();
	CartFileHandler.saveCart(items, username);
    }
}
