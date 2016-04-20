package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Sale;

public interface SaleService {
	public Sale selectSale(long id) throws Exception;
	
	public List<Sale> selectSales(Map<String, Object> map) throws Exception;
	
	public List<Sale> selectSalesByStatus(String status) throws Exception;

	public void createSale(Sale sale) throws Exception;
	
	public void editSale(Sale sale) throws Exception;
	
	public void deleteSale(long id) throws Exception;
}
