package ssp.assignment3.server.data.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName = "products")
public class ProductCollection {

	public ProductCollection() {
		// TODO Auto-generated constructor stub
	}
	
	@JacksonXmlProperty(localName = "product")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Product> productList = new ArrayList<Product>();
	
	public List<Product> getProductList(){
		return productList;
	}
	
	public void setProductList(List<Product> products) {
		this.productList = products;
	}

}
