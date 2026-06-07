package eos.core;
import java.util.*;
import java.io.*;

public class OrderFileHandler {
    private static String orderPath ( String username ) {
	return "data/orders/" + username + ".txt";
    }

    public static void saveOrder ( Order order ) {
	try (BufferedWriter bw = new BufferedWriter(new FileWriter(orderPath(order.getUsername())))) {
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
}
