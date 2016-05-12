package junstech.batch;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import junstech.util.BackupUtil;
import junstech.util.EmailUtil;
import junstech.util.LanguageUtil;

@Component
public class BackupHandler {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

	@Scheduled(cron = "0 * *  * * ? ") // execute every minute
	public void NewFinanceReceviableVerifyHandling() throws Exception{
		InputStream is = BackupUtil.class.getClassLoader().getResourceAsStream("config.properties");
		Properties properties = new Properties();
		properties.load(is);
		String DBBackup = BackupUtil.export(properties);
		if(DBBackup == null){
			EmailUtil.sendMailToOwnCompany("Failed to Backup Database", "Please contact support team for help.");
		}else{
			EmailUtil.sendMailToOwnCompany("Backup database for " + dateFormat.format(new Date()), "Please find the backup in the attachment", new File(DBBackup));
		}
		
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static boolean runAble = true;
}
