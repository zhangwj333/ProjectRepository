package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Purchase;

public interface PurchaseService {
	public Purchase selectPurchase(long id) throws Exception;
	
	public List<Purchase> selectPurchasesByStatus(String status) throws Exception;
	
	public List<Purchase> selectPurchases(Map<String, Object> map) throws Exception;
	
	public void createPurchase(Purchase purchase) throws Exception;
	
	public void editPurchase(Purchase purchase) throws Exception;
	
	public void deletePurchase(long id) throws Exception;
}
