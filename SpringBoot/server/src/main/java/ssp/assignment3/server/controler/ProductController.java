package ssp.assignment3.server.controler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ssp.assignment3.server.data.model.Product;
import ssp.assignment3.server.data.model.ProductCollection;
import ssp.assignment3.server.db.Database;

@RestController
@RequestMapping("/products")
public class ProductController {

	@GetMapping(value = "/{unit_price}", produces = MediaType.APPLICATION_XML_VALUE)
	public ProductCollection getProduct(@PathVariable("unit_price") double unit_price) {
		List<Product> result = Database.searchByPrice(unit_price);
		ProductCollection productCollection = new ProductCollection();
		productCollection.setProductList(result);
		return productCollection;
	}
	
	@GetMapping(value = "/cheapest", produces=MediaType.APPLICATION_XML_VALUE)
	public Product getCheapest() {
		return Database.getCheapest();
	}
	
	@GetMapping(value = "/expensive&cheapest", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Product> getExpensiveAndCheapest() {
		Product cheapest = Database.getCheapest();
		Product expensive = Database.getExpensive();
		Map<String, Product> map = new ConcurrentHashMap<>();
		map.put("expensive", expensive);
		map.put("cheapest", cheapest);
		return map;
	}
	
	@GetMapping(value = "/list", produces=MediaType.APPLICATION_XML_VALUE)
	public ProductCollection getList() {
		List<Product> list = Database.list();
		ProductCollection productCollection = new ProductCollection();
		productCollection.setProductList(list);
		return productCollection;
	}
	
}
