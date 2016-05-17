package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Product;

public interface ProductService {
	
	public Product selectProduct(int id) throws Exception;
	
	public List<Product> selectAllProducts() throws Exception;
	
	public List<Product> selectProducts(Map<String, Object> map) throws Exception;

	public void createProduct(Product product) throws Exception;
	
	public void editProduct(Product product) throws Exception;
	
	public void deleteProduct(int id) throws Exception;
}
