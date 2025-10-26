package ssp.assignment3.server.data.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "product")
public class Product {
	private String name;
	private String unit_price;
	private String amount;
	
	public Product() {
		this.name = "no_name";
		this.unit_price = "no_unit_price";
		this.amount = "no_amount";
	}
	public Product(String name, String unit_price, String amount) {
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

	public String getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "Product: " + name +"-> "+unit_price+" "+amount;
	}

}
