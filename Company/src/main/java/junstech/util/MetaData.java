package junstech.util;


public class MetaData {

	public static Integer FinanceMgrModule = 1;
	public static Integer UserMgrModule = 2;
	public static Integer PurchaseMgrModule = 3;
	public static Integer SaleMgrModule = 4;
	public static Integer InventoryMgrModule = 5;
	public static Integer ReportMgrModule = 6;

	public static Integer FinanceMainIncome = 1;
	
	public static String[] saleStatuses = { //
			"已拒绝", //
			"新开单", //
			"待确认", //			
			"待出库", //
			"已出库", //
			"已到货", //
			"已核销"  //
	};

	public static String[] purchaseStatuses = { //
			"新开单", //
			"待确认", //
			"待出库", //
			"运输中", //
			"已入库"  //
	};
	
	public static String completeReturnPage = "page";

	public static String setNoteTitle = "title";
	public static String setNoteType = "type";
	public static String setTargetFrame = "target";
	public static String setTargetAsFullWindow = "_parent";
	public static String setTargetAsContentFrame = "contentFrame";

	public static String cosmoDefault = "default";
	public static String cosmoPrimary = "primary";
	public static String cosmoSuccess = "success";
	public static String cosmoInfo = "info";
	public static String cosmoWarning = "warning";
	public static String cosmoDanger = "danger";
	
	public static String cargoPath;
	public static String logPath;
	public static String transactionPath;
	public static String reportPath;
}
