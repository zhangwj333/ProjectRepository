package junstech.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.ProductMapper;
import junstech.model.Product;
import junstech.service.ProductService;

@Transactional
@Service("productService")
public class ProductServiceImp implements ProductService{
	
	ProductMapper productMapper;
	
	public ProductMapper getProductMapper() {
		return productMapper;
	}

	@Autowired
	public void setProductMapper(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Product selectProduct(int id) throws Exception {
		return productMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Product> selectAllProducts() throws Exception {
		return productMapper.selectAll();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Product> selectProducts(Map<String, Object> map) throws Exception {
		return productMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createProduct(Product product) throws Exception {
		productMapper.insert(product);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editProduct(Product product) throws Exception {
		productMapper.updateByPrimaryKey(product);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteProduct(int id) throws Exception {
		productMapper.deleteByPrimaryKey(id);		
	}
	

}
