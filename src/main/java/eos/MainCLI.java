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
	CartFileRead.saveCart( cart.getItems(), "testuser");
	System.out.println("Cart saved");

	// test load cart
	List<Product> loaded = CartFileRead.loadCart("testuser");
	for (Product p : loaded) {
	    System.out.println(p.getName() + " " + p.getPrice());
	}
    }
}
