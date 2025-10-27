package ssp.assignment3.server.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ssp.assignment3.server.data.model.Product;

public class Database {
	private static List<Product> products = new ArrayList<>();
	static {
		products.add(new Product("apple",0.56,100));
		products.add(new Product("lingonberry",3,200));
		products.add(new Product("strawberry",4,300));
		products.add(new Product("kiwi",4,300));
	}
	public static List<Product> searchByPrice(double unit_price) {
		return products.stream()
				.filter(p -> p.getUnit_price() == unit_price)
				.collect(Collectors.toList());
	}
	public static List<Product> list(){
		return products;
	}
	
	public static Product getCheapest(){
		return products.stream()
				.min(Comparator.comparing(Product::getUnit_price))
				.orElse(new Product());
	}
	
	public static Product getExpensive() {
		return products.stream()
				.max(Comparator.comparing(Product::getUnit_price))
				.orElse(new Product());
	}
}
