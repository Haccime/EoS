package eos;

import eos.core.*;

import java.io.File;
import java.util.*;

public class MainCLI {
	public static void main(String[] args) {
		Inventory inventory = new Inventory();
		Cart cart = new Cart();

		// test adding to cart
		cart.addItem(inventory.getAll().get(0));

		// test save cart
		CartFileHandler.saveCart(cart.getItems(), "testuser");
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

		// get all usernames from orders folder
		File ordersFolder = new File(System.getProperty("user.dir") + "/data/orders/");
		File[] orderFiles = ordersFolder.listFiles();

		if (orderFiles != null) {
			for (File file : orderFiles) {
				String username = file.getName().replace(".txt", "");
				List<Order> orders = OrderFileHandler.loadOrders(username);
				for (Order o : orders) {
					String itemNames = "";
					for (Product p : o.getItems()) {
						itemNames += p.getName() + ", ";
					}
					itemNames = itemNames.replaceAll(", $", "");
					System.out.println(username + " | " + itemNames + " | Total: RM"
							+ o.getTotal() + " [" + o.getTimestamp() + "]");
				}
			}
		}

	}
}
