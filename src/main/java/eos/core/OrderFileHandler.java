package eos.core;
import java.util.*;
import java.io.*;

public class OrderFileHandler {
    private static String orderPath ( String username ) {
	return "data/orders/" + username + ".txt";
    }

    public static void saveOrder ( Order order ) {
	try (BufferedWriter bw = new BufferedWriter(new FileWriter(orderPath(order.getUsername()),true))) {
	    for ( Product p : order.getItems()) {
		bw.write(p.getCode() + "," + p.getName() + "," + p.getPrice());
		bw.newLine();
	    }
	    bw.write("TOTAL," + order.getTotal() + "," + order.getTimestamp()); // summary of the order
	    bw.newLine();
	    bw.write("---"); // separator for every new order
	    bw.newLine();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }

	public static List<Order> loadOrders(String username) {
    List<Order> orders = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(orderPath(username)))) {
        String line;
        List<Product> items = new ArrayList<>();
        double total = 0;
        String timestamp = "";

        while ((line = br.readLine()) != null) {
            if (line.equals("---")) {
                // End of one order block
                orders.add(new Order(username, items, total, timestamp));
                items = new ArrayList<>();
            } else if (line.startsWith("TOTAL,")) {
                String[] parts = line.split(",");
                total = Double.parseDouble(parts[1]);
                timestamp = parts[2];
            } else {
                String[] parts = line.split(",");
                items.add(new Product(parts[1], parts[0], Double.parseDouble(parts[2]),0));
            }
        }
    } catch (IOException e) {
        	e.printStackTrace();
    	}
    	return orders;
	}
}
