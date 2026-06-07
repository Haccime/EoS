package eos;
import eos.core.*;
import java.util.*;

public class MainCLI {
    public static void main(String[] args) {
	Inventory inventory = new Inventory();
	Cart cart = new Cart();

	// test adding to cart
	cart.addItem(inventory.getAll().get(0));

	// test save cart
	CartFileHandler.saveCart( cart.getItems(), "testuser");
	System.out.println("Cart saved");

	// test load cart
	List<Product> loaded = CartFileHandler.loadCart("testuser");
	for (Product p : loaded) {
	    System.out.println(p.getName() + " " + p.getPrice());
	}

	// Testing order class
	List<Product> items = new ArrayList<>();
	items.add(inventory.getAll().get(0));
	Order order = new Order("testuser", items, 2.00);
	OrderFileHandler.saveOrder(order);
	System.out.println("Order saved");
    }
}
