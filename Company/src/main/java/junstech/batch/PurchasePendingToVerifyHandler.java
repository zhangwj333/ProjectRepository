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
			List<Purchase> purchases = purchaseService.selectPurchasesByStatus("��ȷ��");
			if (purchases.size() > 0) {
				for (Purchase purchase : purchases) {
					LogUtil.logger.info("ִ�����۶���:" + purchase.getId());

					Inventory inventory = new Inventory();
					inventory.setGoodid(purchase.getGoodid());
					inventory.setGoodsortid(purchase.getGood().getGoodsortid());
					inventory.setStatus("������");
					inventory.setType("�ڻ�");
					inventory.setPrice(purchase.getPrice());
					inventory.setInventoryqty(purchase.getGoodqty());
					inventory.setActionid("purchase" + purchase.getId());
					inventoryService.createInventory(inventory);
					purchase.setStatus("������");
					purchase.setNote(purchase.getNote().concat("<br/>" + df.format(new Date()) + ": ������-����ѽ���"));
					purchaseService.editPurchase(purchase);
				}
			}
		} catch (Exception e) {
			LogUtil.logger.error(e.getStackTrace().toString());
		}
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
}
