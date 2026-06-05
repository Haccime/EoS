package eos;
import eos.core.*;

public class MainCLI {
    public static void main(String[] args) {
	int count = 0;
	Inventory inventory = new Inventory();

	for ( Product p : inventory.getAll()) {
	    count++;
	    System.out.print(count + " ");
	    System.out.println(p.getName());
	}
    }
}
