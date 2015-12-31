package junstech.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.InventoryMapper;
import junstech.model.Inventory;
import junstech.service.InventoryService;

@Transactional
@Service("inventoryService")
public class InventoryServiceImp implements InventoryService{

	InventoryMapper inventoryMapper;
		
	public InventoryMapper getInventoryMapper() {
		return inventoryMapper;
	}

	@Autowired
	public void setInventoryMapper(InventoryMapper inventoryMapper) {
		this.inventoryMapper = inventoryMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Inventory selectInventory(long id) throws Exception {	
		return inventoryMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Inventory> selectInventorysForBatch() throws Exception {
		return inventoryMapper.selectForVerifyBatch();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Inventory> selectInventorys(Map<String, Object> map) throws Exception {
		return inventoryMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Inventory> selectSummary(Map<String, Object> map) throws Exception {
		return inventoryMapper.selectSummaryByPage(map);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Inventory selectInventory(int id, String type) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", type);
		return inventoryMapper.selectSummary(map);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createInventory(Inventory inventory) throws Exception {
		inventoryMapper.insert(inventory);	
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editInventory(Inventory inventory) throws Exception {
		inventoryMapper.updateByPrimaryKey(inventory);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteInventory(long id) throws Exception {
		inventoryMapper.deleteByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteInventoryByActionId(String id) throws Exception {
		inventoryMapper.deleteByActionId(id);
	}
	
}
