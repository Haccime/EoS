/* Written By
 * DEN MUHAMMAD HAKIM BIN JUMAATUDEN 2514781
 */

package eos.core;
import java.io.*;
import java.util.*;

public class CartFileHandler {

    private static String cartPath ( String username ) {
	return "data/carts/" + username + ".txt";
    }

    public static List<Product> loadCart ( String username ) {
	List<Product> items = new ArrayList<>();

	File file = new File(cartPath(username));
	if (!file.exists()) return items;

	try (BufferedReader br = new BufferedReader(new FileReader(cartPath(username)))) {
	    String line;
	    while ((line = br.readLine()) != null) {
		if (line.trim().isEmpty()) continue;
		String[] parts = line.split(",");
		items.add(new Product(parts[1].trim(), parts[0].trim(), Double.parseDouble(parts[2].trim()), 0));	    }
	}
	catch (IOException e ) {
	    e.printStackTrace();
	}
	return items;
    }

    public static void saveCart ( List<Product> items, String username ) {
	try (BufferedWriter bw = new BufferedWriter(new FileWriter(cartPath(username)))) {
	    for (Product p : items) {
		bw.write(p.getCode() + "," + p.getName() + "," + p.getPrice());
		bw.newLine();
	    }
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
