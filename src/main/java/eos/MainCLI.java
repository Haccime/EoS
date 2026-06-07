package eos;
import eos.core.*;

public class MainCLI {
    public static void main(String[] args) {
	int count = 0;
	Cart cart = new Cart();
	Inventory inventory = new Inventory ();

	cart.addItem(inventory.getAll().get(0));

	System.out.println("Total: RM" + cart.getTotal());

	for ( Product p : cart.getItems()) {
	    count++;
	    System.out.print(count + " ");
	    System.out.println(p.getName());
	}

    }
}
