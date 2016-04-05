package junstech.util;


public class MetaData {

	public final static Integer FinanceMgrModule = 1;
	public final static Integer UserMgrModule = 2;
	public final static Integer PurchaseMgrModule = 3;
	public final static Integer SaleMgrModule = 4;
	public final static Integer InventoryMgrModule = 5;
	public final static Integer ReportMgrModule = 6;

	public final static Integer FinanceMainIncome = 1;
	
	public final static String[] saleStatuses = { //
			"已拒绝", //
			"新开单", //
			"待确认", //			
			"待出库", //
			"已出库", //
			"已到货", //
			"已核销"  //
	};

	public final static String[] purchaseStatuses = { //
			"新开单", //
			"待确认", //
			"待出库", //
			"运输中", //
			"已入库"  //
	};
	
	public final static String completeReturnPage = "page";

	public final static String setNoteTitle = "title";
	public final static String setNoteType = "type";
	public final static String setTargetFrame = "target";
	public final static String setTargetAsFullWindow = "_parent";
	public final static String setTargetAsContentFrame = "contentFrame";

	public final static String cosmoDefault = "default";
	public final static String cosmoPrimary = "primary";
	public final static String cosmoSuccess = "success";
	public final static String cosmoInfo = "info";
	public final static String cosmoWarning = "warning";
	public final static String cosmoDanger = "danger";
	
	public static String cargoPath;
	public static String logPath;
	public static String transactionPath;
	public static String reportPath;
	
	public final static String ProcessResult = "RESULT";
	public final static String ProcessSuccess = "SUCCESS";
	public final static String ProcessFail = "FAIL";
	public final static String ProcessTimeout = "TIMEOUT";
	
	public final static double initfund = 5000000;
}
