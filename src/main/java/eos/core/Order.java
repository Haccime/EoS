package eos.core;
import java.util.*;

public class Order {
    private String username;
    private List<Product> items;
    private double total;
    private String timestamp;

    public Order(String username, List<Product> items, double total) {
        this.username = username;
        this.items = items;
        this.total = total;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // Getters
    public String getUsername() { return username; }
    public List<Product> getItems() { return items; }
    public double getTotal() { return total; }
    public String getTimestamp() { return timestamp; }
}
