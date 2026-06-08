/* Written By
 * DEN MUHAMMAD HAKIM BIN JUMAATUDEN 2514781
 */

package eos.core;

public class Product {
    private String name;
    private String code;
    private double price;
    private int stock;

    public Product () {
	this.name = "";
	this.code = "";
	this.price = 0;
	this.stock = 0;
    }

    public Product (String name, String code, double price, int availability) {
	this.name = name;
	this.code = code;
	this.price = price;
	this.stock = availability;
    }

    // Getters
    public String getName() { return name; }
    public String getCode() { return code; }
    public double getPrice() { return price; }
    public int getAvailability() { return stock; }

    public void reduceStock ( int quantity ) {
	this.stock -= quantity;
    }

    public void addStock ( int quantity ) {
	this.stock += quantity;
    }
}
