package junstech.batch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import junstech.model.Customer;
import junstech.model.Financereceivable;
import junstech.model.Inventory;
import junstech.model.Ledger;
import junstech.model.Purchase;
import junstech.model.Sale;
import junstech.model.Salesub;
import junstech.service.CustomerService;
import junstech.service.FinancereceivableService;
import junstech.service.InventoryService;
import junstech.service.LedgerService;
import junstech.service.PurchaseService;
import junstech.service.SaleService;
import junstech.util.LogUtil;
import junstech.util.MetaData;

@Component
public class FinanceReceviableVerifyHandler {

	FinancereceivableService financereceivableService;

	LedgerService ledgerService;

	CustomerService customerService;

	SaleService saleService;

	public SaleService getSaleService() {
		return saleService;
	}

	@Autowired
	public void setSaleService(SaleService saleService) {
		this.saleService = saleService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	@Autowired
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

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

	@Scheduled(cron = "0/30 * *  * * ? ") // execute every 5 second
	public void FinanceReceviableVerifyHandling() throws Exception {
		try {
			List<Customer> customers = customerService.selectAllCustomers();
			LogUtil.logger.info("执行应收账整理.........");
			for (Customer customer : customers) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("companyid", customer.getId());
				List<Financereceivable> financereceivables = financereceivableService.selectAllByFields(map);
				map.put("companytype", "customer");
				map.put("financetype", MetaData.FinanceMainIncome);
				List<Ledger> ledgers = ledgerService.selectTotalByFields(map);

				if (financereceivables == null || financereceivables.size() == 0 || ledgers.get(0).getAmount() == null
						|| ledgers.get(0) == null || ledgers.size() != 1) {
					continue;
				}

				double total = ledgers.get(0).getAmount();
				for (Financereceivable financereceivable : financereceivables) {
					if (total == 0) {
						break;
					} else if (total >= financereceivable.getTotalamount()) {
						if (financereceivable.getType().equals("未结清")) {
							financereceivable.setNowpay(financereceivable.getTotalamount());
							financereceivable.setType("已结清");
							financereceivable
									.setNote(financereceivable.getNote() + "<br/>" + df.format(new Date()) + ": 已结清");
							financereceivableService.editFinancereceivable(financereceivable);
							Sale sale = saleService.selectSale(financereceivable.getSaleid());
							sale.setStatus("已完成");
							sale.setNote(sale.getNote() + "<br/>" + df.format(new Date()) + ": 已完成收款");
							saleService.editSale(sale);
						}
						total = total - financereceivable.getTotalamount();
					} else {
						if (total != financereceivable.getNowpay()) {
							financereceivable.setType("未结清");
							financereceivable.setNowpay(total);
							financereceivable.setNote(
									financereceivable.getNote() + "<br/>" + df.format(new Date()) + ": 已结" + total);
							financereceivableService.editFinancereceivable(financereceivable);
						}
						total = 0;
					}
				}

			}
		} catch (Exception e) {
			LogUtil.logger.error(e.getStackTrace().toString());
		}
		LogUtil.logger.info("完成应收账整理.........");
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
}
