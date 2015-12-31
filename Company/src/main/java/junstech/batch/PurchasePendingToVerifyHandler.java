package junstech.batch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import junstech.model.Inventory;
import junstech.model.Purchase;
import junstech.service.InventoryService;
import junstech.service.PurchaseService;
import junstech.util.LogUtil;

@Component
public class PurchasePendingToVerifyHandler {

	PurchaseService purchaseService;

	InventoryService inventoryService;

	public InventoryService getInventoryService() {
		return inventoryService;
	}

	@Autowired
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	public PurchaseService getPurchaseService() {
		return purchaseService;
	}

	@Autowired
	public void setPurchaseService(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}

	@Scheduled(cron = "0/10 * *  * * ? ") // execute every 5 second
	public void NewPurchaseOrderHandling() {
		try {
			List<Purchase> purchases = purchaseService.selectPurchasesByStatus("待确认");
			if (purchases.size() > 0) {
				for (Purchase purchase : purchases) {
					LogUtil.logger.info("执行销售订单:" + purchase.getId());

					Inventory inventory = new Inventory();
					inventory.setGoodid(purchase.getGoodid());
					inventory.setGoodsortid(purchase.getGood().getGoodsortid());
					inventory.setStatus("运送中");
					inventory.setType("期货");
					inventory.setPrice(purchase.getPrice());
					inventory.setInventoryqty(purchase.getGoodqty());
					inventory.setActionid("purchase" + purchase.getId());
					inventoryService.createInventory(inventory);
					purchase.setStatus("运送中");
					purchase.setNote(purchase.getNote().concat("<br/>" + df.format(new Date()) + ": 待出库-库存已结算"));
					purchaseService.editPurchase(purchase);
				}
			}
		} catch (Exception e) {
			LogUtil.logger.error(e.getStackTrace().toString());
		}
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
}
