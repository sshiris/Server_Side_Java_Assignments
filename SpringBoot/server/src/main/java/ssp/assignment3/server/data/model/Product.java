package ssp.assignment3.server.data.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "product")
public class Product {
	private String name;
	private double unit_price;
	private double amount;
	
	public Product() {
		this.name = "no_name";
		this.unit_price = 0;
		this.amount = 0;
	}
	public Product(String name, double unit_price, double amount) {
		this.name = name;
		this.unit_price = unit_price;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "Product: " + name +"-> "+unit_price+" "+amount;
	}

}
