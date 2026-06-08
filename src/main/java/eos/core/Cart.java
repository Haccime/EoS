/* Written By
 * DEN MUHAMMAD HAKIM BIN JUMAATUDEN 2514781
 */

package eos.core;
import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class Cart {
    private final ArrayList<Product> items;

    public Cart ( List<Product> items ) {
	this.items = new ArrayList<>(items);
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
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	String timestamp = LocalDateTime.now().format(formatter);
	Order order = new Order(username, new ArrayList<>(items), getTotal(), timestamp);

	OrderFileHandler.saveOrder(order);
	for ( Product p : items ) {
	    inventory.reduceStock(p.getCode(), 1);
	}
	items.clear();
	CartFileHandler.saveCart(items, username);
    }
}
