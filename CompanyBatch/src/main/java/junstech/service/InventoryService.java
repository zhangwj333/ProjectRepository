package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Inventory;

public interface InventoryService {
	public Inventory selectInventory(long id) throws Exception;
	
	public List<Inventory> selectInventorysForBatch() throws Exception;
	
	public List<Inventory> selectInventorys(Map<String, Object> map) throws Exception;
	
	public List<Inventory> selectSummary(Map<String, Object> map) throws Exception;
	
	public Inventory selectInventory(int id, String status) throws Exception;
	
	public void createInventory(Inventory inventory) throws Exception;
	
	public void editInventory(Inventory inventory) throws Exception;
	
	public void deleteInventory(long id) throws Exception;
	
	public void deleteInventoryByActionId(String id) throws Exception;
}
