package junstech.service.imp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.PurchaseMapper;
import junstech.model.Purchase;
import junstech.service.InventoryService;
import junstech.service.PurchaseService;

@Transactional
@Service("purchaseService")
public class PurchaseServiceImp implements PurchaseService{

	PurchaseMapper purchaseMapper;
	
	InventoryService inventoryService;
	
		
	public InventoryService getInventoryService() {
		return inventoryService;
	}

	@Autowired
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	public PurchaseMapper getPurchaseMapper() {
		return purchaseMapper;
	}

	@Autowired
	public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
		this.purchaseMapper = purchaseMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Purchase selectPurchase(long id) throws Exception {	
		return purchaseMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Purchase> selectPurchasesByStatus(String status) throws Exception{
		return purchaseMapper.selectPurchasesByStatus(status);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Purchase> selectPurchases(Map<String, Object> map) throws Exception {
		return purchaseMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createPurchase(Purchase purchase) throws Exception {
		purchase.setPurchasedate(new Date());
		purchase.setGoodspec("");
		purchaseMapper.insert(purchase);	
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editPurchase(Purchase purchase) throws Exception {
		purchaseMapper.updateByPrimaryKey(purchase);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deletePurchase(long id) throws Exception {
		purchaseMapper.deleteByPrimaryKey(id);
		inventoryService.deleteInventoryByActionId("purchase" + id);
	}

	
}
