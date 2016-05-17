package junstech.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;

import junstech.model.Financereceivable;
import junstech.model.Paymentaccount;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEventAfterSplit;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFReport {

	public static String DEST = "";

	public static String TEXT = "";

	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new PDFReport().createPdf(DEST);
	}
	
	@SuppressWarnings("unchecked")
	public static String generateReport(Map<String, Object> reportFactors) throws IOException, DocumentException {
		Map<String, Double> financeSumMonth = (HashMap<String, Double>) reportFactors.get("financeSumMonth");
		Map<String, Double> financeSumYear = (HashMap<String, Double>) reportFactors.get("financeSumYear");
		List<Financereceivable> financereceivables = (List<Financereceivable>) reportFactors.get("financereceivables");
		Map<String, Object> payaccounts = (Map<String, Object>) reportFactors.get("payaccounts");
		List<Paymentaccount> paymentaccounts = (List<Paymentaccount>) reportFactors.get("paymentaccounts");
		
		String relativeName = "/baobiao" + df.format(new Date()) + ".pdf";
		String fileName = MetaData.reportPath + relativeName;
		File file = new File(fileName);
		file.getParentFile().mkdirs();
		new PDFReport().createSummaryPdf(fileName, getReportData(financeSumMonth), getReportData(financeSumYear),
				financereceivables, payaccounts, paymentaccounts);
		return relativeName;
	}

	public static List<String> getReportData(Map<String, Double> financeSum) {
		List<String> list = new ArrayList<String>();
		double mainIncome = financeSum.containsKey("主营业务收入") ? financeSum.get("主营业务收入") : Double.valueOf(0);
		double mainCost = financeSum.containsKey("主营业务成本") ? financeSum.get("主营业务成本") : Double.valueOf(0);
		double taxOrOthers = financeSum.containsKey("主营业务税金及附加") ? financeSum.get("主营业务税金及附加") : Double.valueOf(0);
		double mainProfit = mainIncome + mainCost + taxOrOthers;
		double otherIncome = financeSum.containsKey("其他业务利润") ? financeSum.get("其他业务利润") : Double.valueOf(0);
		double operCost = financeSum.containsKey("营业费用") ? financeSum.get("营业费用") : Double.valueOf(0);
		double adminCost = financeSum.containsKey("管理费用") ? financeSum.get("管理费用") : Double.valueOf(0);
		double financeCost = financeSum.containsKey("财务费用") ? financeSum.get("财务费用") : Double.valueOf(0);
		double operProfit = mainProfit + otherIncome + operCost + adminCost + financeCost;
		double investIncome = financeSum.containsKey("投资收益") ? financeSum.get("投资收益") : Double.valueOf(0);
		double operExcludeIncome = financeSum.containsKey("营业外收入") ? financeSum.get("营业外收入") : Double.valueOf(0);
		double operExcludeCost = financeSum.containsKey("营业外支出") ? financeSum.get("营业外支出") : Double.valueOf(0);
		double totalProfit = operProfit + investIncome + operExcludeIncome + operExcludeCost;
		double incomeTax = financeSum.containsKey("所得税") ? financeSum.get("所得税") : Double.valueOf(0);
		double retainedProfit = totalProfit - incomeTax;
		list.add(String.valueOf(mainIncome));
		list.add(String.valueOf(mainCost));
		list.add(String.valueOf(taxOrOthers));
		list.add(String.valueOf(mainProfit));
		list.add(String.valueOf(otherIncome));
		list.add(String.valueOf(operCost));
		list.add(String.valueOf(adminCost));
		list.add(String.valueOf(financeCost));
		list.add(String.valueOf(operProfit));
		list.add(String.valueOf(investIncome));
		list.add(String.valueOf(operExcludeIncome));
		list.add(String.valueOf(operExcludeCost));
		list.add(String.valueOf(totalProfit));
		list.add(String.valueOf(incomeTax));
		list.add(String.valueOf(retainedProfit));
		return list;
	}

	public void createSummaryPdf(String path, List<String> financeSumMonth, List<String> financeSumYear,
			List<Financereceivable> financereceivables, Map<String, Object> payaccounts, List<Paymentaccount> paymentaccounts) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(path));
		document.open();
		
		fontBlack = new Font(BaseFont.createFont(PDFReport.class.getClassLoader().getResource("AdobeSongStd-Light.otf").getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), 12, Font.NORMAL, BaseColor.BLACK);
		fontRed = new Font(BaseFont.createFont(PDFReport.class.getClassLoader().getResource("AdobeSongStd-Light.otf").getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), 12, Font.NORMAL, BaseColor.RED);
		
		double yearEndProfit =  createProfitReport(document, financeSumMonth, financeSumYear);
		double financeReceivableTotal = 0;
		if ((!financereceivables.isEmpty()) || financereceivables.get(0) != null
				|| financereceivables.get(0).getId() != null) {
			financeReceivableTotal = createPendingReport(document, financereceivables); 
		}
		createPayAccountBalanceReport(document, payaccounts, paymentaccounts);
		createBalanceReport(document, yearEndProfit, financeReceivableTotal);
		document.close();
	}

	public void createPayAccountBalanceReport(Document document, Map<String, Object> payaccounts, List<Paymentaccount> paymentaccounts)
			throws IOException, DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setTotalWidth(500);
		table.setLockedWidth(true);
		BorderEvent event = new BorderEvent();
		table.setTableEvent(event);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setSplitLate(false);
		
		PdfPCell cell = new PdfPCell(new Phrase(TEXT, fontBlack));
		cell.setBorder(Rectangle.NO_BORDER);
		document.add(new Phrase("\r\n\r\n交易账号余额:\r\n", fontBlack));
		cell.setPhrase(new Phrase("账号", fontBlack));
		table.addCell(cell);
		cell.setPhrase(new Phrase("余额", fontBlack));
		table.addCell(cell);
		for (int i = 0; i < paymentaccounts.size(); i++) {
			cell.setPhrase(new Phrase(paymentaccounts.get(i).getPayaccount(), fontBlack));
			table.addCell(cell);
			cell.setPhrase(new Phrase(String.valueOf(payaccounts.get(paymentaccounts.get(i).getPayaccount())),
					fontBlack));
			table.addCell(cell);
		}
		event.setRowCount(table.getRows().size());
		document.add(table);
	}
	
	public void createBalanceReport(Document document, double yearEndProfit, double financeReceivableTotal)
			throws IOException, DocumentException {
		double balance = MetaData.initfund + yearEndProfit - financeReceivableTotal;
		document.add(new Phrase("\r\n\r\n公司剩余资金: " + balance, fontBlack));
	}
	
	public double createPendingReport(Document document, List<Financereceivable> financereceivables)
			throws IOException, DocumentException {
		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(500);
		table.setLockedWidth(true);
		BorderEvent event = new BorderEvent();
		table.setTableEvent(event);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setSplitLate(false);

		PdfPCell cell = new PdfPCell(new Phrase(TEXT, fontBlack));
		cell.setBorder(Rectangle.NO_BORDER);

		document.add(new Phrase("\r\n\r\n剩余应收账\r\n记账日期:" + dftime.format(new Date()) + "\r\n", fontBlack));
		cell.setPhrase(new Phrase("公司", fontBlack));
		table.addCell(cell);
		cell.setPhrase(new Phrase("订单总数", fontBlack));
		table.addCell(cell);
		cell.setPhrase(new Phrase("剩余应收", fontBlack));
		table.addCell(cell);
		double financeReceivableTotal= 0;
		for (int i = 0; i < financereceivables.size(); i++) {
			cell.setPhrase(new Phrase(financereceivables.get(i).getCustomer().getName(), fontBlack));
			table.addCell(cell);
			cell.setPhrase(new Phrase(String.valueOf(financereceivables.get(i).getTotalamount()), fontBlack));
			table.addCell(cell);
			cell.setPhrase(new Phrase(
					String.valueOf(financereceivables.get(i).getTotalamount() - financereceivables.get(i).getNowpay()),
					fontBlack));
			table.addCell(cell);
			financeReceivableTotal = financeReceivableTotal +  financereceivables.get(i).getTotalamount() - financereceivables.get(i).getNowpay();
		}
		event.setRowCount(table.getRows().size());
		document.add(table);
		return Double.valueOf(financeReceivableTotal);
	}

	public double createProfitReport(Document document, List<String> financeSumMonth, List<String> financeSumYear)
			throws IOException, DocumentException {
		List<String> list = new ArrayList<String>();
		list.add("一、主营业务收入");
		list.add("   减: 主营业务成本");
		list.add("          主营业务税金及附加");
		list.add("二、主营业务利润");
		list.add("   加: 其他业务利润");
		list.add("   减: 营业费用");
		list.add("          管理费用");
		list.add("          财务费用");
		list.add("三、营业利润");
		list.add("   加: 投资收益");
		list.add("          营业外收入");
		list.add("   减: 营业外支出");
		list.add("四、利润总额");
		list.add("   减: 所得税");
		list.add("五、净利润");

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(500);
		table.setLockedWidth(true);
		BorderEvent event = new BorderEvent();
		table.setTableEvent(event);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setSplitLate(false);
		
		PdfPCell cell = new PdfPCell(new Phrase(TEXT, fontBlack));
		cell.setBorder(Rectangle.NO_BORDER);
		String date = dftime.format(new Date());
		String[] dateInfo = date.split("-");
		String month;
		if ((Integer.parseInt(dateInfo[1]) - 1) == 0) {
			dateInfo[0] = String.valueOf(Integer.parseInt(dateInfo[0]) - 1);
			month = "12";
		} else {
			month = StringUtils.leftPad(String.valueOf(Integer.parseInt(dateInfo[1]) - 1), 2, "0");
		}
		document.add(new Phrase(dateInfo[0] + month + "报表\r\n制表日期:" + dftime.format(new Date()) + "\r\n", fontBlack));
		cell.setPhrase(new Phrase("项目", fontBlack));
		table.addCell(cell);
		cell.setPhrase(new Phrase("本月数", fontBlack));
		table.addCell(cell);
		cell.setPhrase(new Phrase("本年累计数", fontBlack));
		table.addCell(cell);
		for (int i = 0; i < list.size(); i++) {
			cell.setPhrase(new Phrase(list.get(i), fontBlack));
			table.addCell(cell);
			cell.setPhrase((new Phrase(financeSumMonth.get(i).replaceFirst("-", ""),
					financeSumMonth.get(i).startsWith("-") ? fontRed : fontBlack)));
			table.addCell(cell);
			cell.setPhrase(
					(new Phrase(financeSumMonth.get(i).replaceFirst("-", ""), financeSumYear.get(i).startsWith("-") ? fontRed : fontBlack)));
			table.addCell(cell);
		}
		event.setRowCount(table.getRows().size());
		document.add(table);
		return Double.valueOf(financeSumYear.get(financeSumYear.size()-1));
	}

	class BorderEvent implements PdfPTableEventAfterSplit {

		protected int rowCount;
		protected boolean bottom = true;
		protected boolean top = true;

		public void setRowCount(int rowCount) {
			this.rowCount = rowCount;
		}

		public void splitTable(PdfPTable table) {
			if (table.getRows().size() != rowCount) {
				bottom = false;
			}
		}

		public void afterSplitTable(PdfPTable table, PdfPRow startRow, int startIdx) {
			if (table.getRows().size() != rowCount) {
				// if the table gains a row, a row was split
				rowCount = table.getRows().size();
				top = false;
			}
		}

		public void tableLayout(PdfPTable table, float[][] width, float[] height, int headerRows, int rowStart,
				PdfContentByte[] canvas) {
			float widths[] = width[0];
			float y1 = height[0];
			float y2 = height[height.length - 1];
			PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
			for (int i = 0; i < widths.length; i++) {
				cb.moveTo(widths[i], y1);
				cb.lineTo(widths[i], y2);
			}
			float x1 = widths[0];
			float x2 = widths[widths.length - 1];
			for (int i = top ? 0 : 1; i < (bottom ? height.length : height.length - 1); i++) {
				cb.moveTo(x1, height[i]);
				cb.lineTo(x2, height[i]);
			}
			cb.stroke();
			cb.resetRGBColorStroke();
			bottom = true;
			top = true;
		}
	}

	public void createPdf(String dest) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(500);
		table.setLockedWidth(true);
		BorderEvent event = new BorderEvent();
		table.setTableEvent(event);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setSplitLate(false);

		PdfPCell cell = new PdfPCell(new Phrase(TEXT, fontBlack));
		cell.setBorder(Rectangle.NO_BORDER);
		for (int i = 0; i < 60;) {
			table.addCell("Cell " + (++i));
			table.addCell(cell);
		}
		event.setRowCount(table.getRows().size());
		document.add(table);
		document.close();
	}

	private Font fontBlack = null;
	private Font fontRed = null;
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
	private static SimpleDateFormat dftime = new SimpleDateFormat("yyyy-MM-dd");

	static {
		
		Properties prop = new Properties();
		System.out.println(PDFReport.class.getClassLoader().getResource("config.properties").getPath());
		try {
			// 读取属性文件a.properties
			InputStream in = PDFReport.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(in); /// 加载属性列表
			DEST = prop.getProperty("report");
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}