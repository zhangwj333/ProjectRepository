package junstech.batch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import junstech.model.Financereceivable;
import junstech.model.Inventory;
import junstech.model.Ledger;
import junstech.model.Purchase;
import junstech.model.Sale;
import junstech.model.Salesub;
import junstech.service.FinancereceivableService;
import junstech.service.InventoryService;
import junstech.service.LedgerService;
import junstech.service.PurchaseService;
import junstech.service.SaleService;
import junstech.util.LogUtil;

@Component
public class InventoryVerifyHandler {

	SaleService saleService;

	PurchaseService purchaseService;

	InventoryService inventoryService;

	FinancereceivableService financereceivableService;

	LedgerService ledgerService;

	public LedgerService getLedgerService() {
		return ledgerService;
	}

	@Autowired
	public void setLedgerService(LedgerService ledgerService) {
		this.ledgerService = ledgerService;
	}

	public FinancereceivableService getFinancereceivableService() {
		return financereceivableService;
	}

	@Autowired
	public void setFinancereceivableService(FinancereceivableService financereceivableService) {
		this.financereceivableService = financereceivableService;
	}

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

	public PurchaseService getPurchaseService() {
		return purchaseService;
	}

	@Autowired
	public void setPurchaseService(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}

	@Scheduled(cron = "0/5 * *  * * ? ") // execute every 5 second
	public void NewSaleOrderHandling() {
		try {
			List<Inventory> inventorys = inventoryService.selectInventorysForBatch();
			for (Inventory inventory : inventorys) {
				if (inventory.getActionid().contains("purchase")) {
					if ((!inventory.getStatus().equals("已入库")) || (!checkFullyComplete(inventory, inventorys))) {
						continue;
					}
					Purchase purchase = purchaseService
							.selectPurchase(Long.parseLong(inventory.getActionid().replace("purchase", "").trim()));
					if (purchase.getStatus().equals("已完成")) {
						continue;
					}
					purchase.setStatus("已完成");
					purchase.setNote(purchase.getNote().concat("<br/>" + df.format(new Date()) + ": 已入库-采购单结束"));
					purchaseService.editPurchase(purchase);
				} else if (inventory.getActionid().contains("sale")) {
					if ((!inventory.getStatus().equals("已出货")) || (!checkFullyComplete(inventory, inventorys))) {
						continue;
					}
					Sale sale = saleService
							.selectSale(Long.parseLong(inventory.getActionid().replace("sale", "").trim()));
					if (sale.getStatus().equals("待收款") || sale.getStatus().equals("已完成")) {
						continue;
					}
					sale.setStatus("待收款");
					sale.setNote(sale.getNote().concat("<br/>" + df.format(new Date()) + ": 已出货"));
					sale.setNote(sale.getNote().concat("<br/>" + df.format(new Date()) + ": 待收款"));
					saleService.editSale(sale);
					// 录入应收账
					Financereceivable financereceivable = new Financereceivable();
					financereceivable.setSaleid(sale.getId());
					financereceivable.setCompanyid(sale.getCustomerid());
					financereceivable.setTotalamount(sale.getTotal());
					financereceivable.setNowpay(Double.valueOf("0"));
					financereceivable.setType("未结清");
					financereceivable.setNote(df.format(new Date()) + ": 生成应收账");
					financereceivableService.createFinancereceivable(financereceivable);
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			LogUtil.logger.error(e.getStackTrace().toString());
		}
	}

	private boolean checkFullyComplete(Inventory inventory, List<Inventory> inventorys) {
		int count = 0;
		for (int i = 0; i < inventorys.size(); i++) {
			if (inventory.getActionid().equals(inventorys.get(i).getActionid())) {
				count++;
			}
		}
		if (count != 1) {
			return false;
		}
		return true;
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
}
