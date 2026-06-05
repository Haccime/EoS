package eos.core;

public class Product {
    private String name;
    private String code;
    private double price;
    private int availability;

    public Product () {
	this.name = "";
	this.code = "";
	this.price = 0;
	this.availability = 0;
    }

    public Product (String name, String code, double price, int availability) {
	this.name = name;
	this.code = code;
	this.price = price;
	this.availability = availability;
    }

    // Getters
    public String getName() { return name; }
    public String getCode() { return code; }
    public double getPrice() { return price; }
    public int getAvailability() { return availability; }
}
