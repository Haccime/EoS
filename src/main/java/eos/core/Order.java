/* Written By
 * DEN MUHAMMAD HAKIM BIN JUMAATUDEN 2514781
 * MUHAMMAD ARIF NAUFAL BIN NAZRI 2516439 
 */

package eos.core;
import java.util.*;

public class Order {
    private String username;
    private List<Product> items;
    private double total;
    private String timestamp;

    public Order(String username, List<Product> items, double total, String timestamp) {
        this.username = username;
        this.items = items;
        this.total = total;
        this.timestamp = timestamp;
    }

    // Getters
    public String getUsername() { return username; }
    public List<Product> getItems() { return items; }
    public double getTotal() { return total; }
    public String getTimestamp() { return timestamp; }
}
