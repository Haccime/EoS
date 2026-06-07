package eos.core;
import java.util.ArrayList;

public class Inventory {
    private ArrayList<Product> products;

    public Inventory () {
	products = new ArrayList<>(ProductFileRead.loadProducts());
    }

    public ArrayList<Product> getAll() {
	return products;
    }

    public void addProduct(Product p) {
	products.add(p);
	ProductFileRead.saveProducts(products);
    }

    public void removeProduct(String code) {
	products.removeIf(p -> p.getCode().equals(code));
	ProductFileRead.saveProducts(products);
    }

    public void addStock(String code, int quantity) {
	for (Product p : products) {
	    if (p.getCode().equals(code)) {
		p.addStock(quantity);
		ProductFileRead.saveProducts(products);
		break;
	    }
	}
    }

    public void reduceStock(String code, int quantity) {
    for (Product p : products) {
        if (p.getCode().equals(code)) {
            p.reduceStock(quantity);
            ProductFileRead.saveProducts(products);
            break;
        }
    }
}
}
