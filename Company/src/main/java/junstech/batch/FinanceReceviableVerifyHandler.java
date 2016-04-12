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
import junstech.util.RedisUtil;

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
	public void NewFinanceReceviableVerifyHandling() throws Exception{
		if(runAble){
			FinanceReceviableVerifyHandling();
		}
	}
	
	public void FinanceReceviableVerifyHandling() throws Exception {
		try {
			runAble = false;
			List<Customer> customers = customerService.selectAllCustomers();
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
						if (financereceivable.getType().equals(RedisUtil.getString("statusPendingPayment"))) {
							financereceivable.setNowpay(financereceivable.getTotalamount());
							financereceivable.setType(RedisUtil.getString("statusCompletePayment"));
							financereceivable
									.setNote(financereceivable.getNote() + "<br/>" + df.format(new Date()) + ": " + RedisUtil.getString("statusCompletePayment"));
							financereceivableService.editFinancereceivable(financereceivable);
							Sale sale = saleService.selectSale(financereceivable.getSaleid());
							sale.setStatus(RedisUtil.getString("statusCompleteOrder"));
							sale.setNote(sale.getNote() + "<br/>" + df.format(new Date()) + ": " + RedisUtil.getString("NoteCompletePayment"));
							saleService.editSale(sale);
						}
						total = total - financereceivable.getTotalamount();
					} else {
						if (total != financereceivable.getNowpay()) {
							financereceivable.setType(RedisUtil.getString("statusPendingPayment"));
							financereceivable.setNowpay(total);
							financereceivable.setNote(
									financereceivable.getNote() + "<br/>" + df.format(new Date()) + ": " + RedisUtil.getString("NoteLeftPayment") + total);
							financereceivableService.editFinancereceivable(financereceivable);
						}
						total = 0;
					}
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
