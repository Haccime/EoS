/* Written By
 * DEN MUHAMMAD HAKIM BIN JUMAATUDEN 2514781
 */

package eos.core;
import java.util.ArrayList;

public class Inventory {
    private ArrayList<Product> products;

    public Inventory () {
	products = new ArrayList<>(ProductFileHandler.loadProducts());
    }

    public ArrayList<Product> getAll() {
	return products;
    }

    public void addProduct(Product p) {
	products.add(p);
	ProductFileHandler.saveProducts(products);
    }

    public void removeProduct(String code) {
	products.removeIf(p -> p.getCode().equals(code));
	ProductFileHandler.saveProducts(products);
    }

    public void addStock(String code, int quantity) {
	for (Product p : products) {
	    if (p.getCode().equals(code)) {
		p.addStock(quantity);
		ProductFileHandler.saveProducts(products);
		break;
	    }
	}
    }

    public void reduceStock(String code, int quantity) {
    for (Product p : products) {
        if (p.getCode().equals(code)) {
            p.reduceStock(quantity);
            ProductFileHandler.saveProducts(products);
            break;
        }
    }
}
}
