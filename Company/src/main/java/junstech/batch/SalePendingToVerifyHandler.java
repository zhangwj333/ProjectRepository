package junstech.batch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import junstech.model.Inventory;
import junstech.model.Sale;
import junstech.model.Salesub;
import junstech.service.InventoryService;
import junstech.service.SaleService;
import junstech.util.LogUtil;
import junstech.util.RedisUtil;

@Component
public class SalePendingToVerifyHandler {

	SaleService saleService;

	InventoryService inventoryService;

	public InventoryService getInventoryService() {
		return inventoryService;
	}

	@Autowired
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	public SaleService getSaleService() {
		return saleService;
	}

	@Autowired
	public void setSaleService(SaleService saleService) {
		this.saleService = saleService;
	}

	@Scheduled(cron = "0/10 * *  * * ? ") // execute every 5 second
	public void NewSaleOrderHandling() throws Exception{
		if(runAble){
			SaleOrderHandling();
		}
	}
	
	public void SaleOrderHandling() {
		try {
			runAble = false;
			List<Sale> sales = saleService.selectSalesByStatus(RedisUtil.getString("statusPendingVerification"));
			if (sales.size() > 0) {
				for (Sale sale : sales) {
					LogUtil.logger.info(RedisUtil.getString("NoteHandleSale") + sale.getId());
					List<Salesub> salesubs = sale.getSalesubs();
					if (sale.getSalesubs().size() <= 0) {
						sale.setStatus("InvalidOrder");
						saleService.editSale(sale);
						continue;
					}
					for (Salesub salesub : salesubs) {
						Inventory inventory = new Inventory();
						inventory.setGoodid(-1);
						inventory.setGoodsortid(salesub.getGoodid());
						inventory.setStatus(RedisUtil.getString("statusPendingShipOutFromInventory"));
						inventory.setType(RedisUtil.getString("futures"));
						inventory.setPrice(salesub.getPrice());
						inventory.setInventoryqty(-salesub.getGoodqty());
						inventory.setExecutedate(salesub.getOpertime());
						inventory.setActionid("sale" + sale.getId());
						inventoryService.createInventory(inventory);
					}
					sale.setStatus(RedisUtil.getString("statusPendingShipOutFromInventory"));
					sale.setNote(sale.getNote().concat("<br/>" + df.format(new Date()) + ": " + (RedisUtil.getString("NotePendingShipOut"))));
					saleService.editSale(sale);
				}
			}
			runAble = true;
		} catch (Exception e) {
			LogUtil.logger.error(e.getStackTrace().toString());
		}
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static boolean runAble = true;
}
