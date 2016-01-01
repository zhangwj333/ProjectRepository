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

	public static String generateReport(Map<String, Double> financeSumMonth, Map<String, Double> financeSumYear,
			List<Financereceivable> financereceivables) throws IOException, DocumentException {
		String relativeName = "/baobiao" + df.format(new Date()) + ".pdf";
		String fileName = MetaData.reportPath + relativeName;
		File file = new File(fileName);
		file.getParentFile().mkdirs();
		new PDFReport().createSummaryPdf(fileName, getReportData(financeSumMonth), getReportData(financeSumYear),
				financereceivables);
		return relativeName;
	}

	public static List<String> getReportData(Map<String, Double> financeSum) {
		List<String> list = new ArrayList<String>();
		double mainIncome = financeSum.containsKey("��Ӫҵ������") ? financeSum.get("��Ӫҵ������") : Double.valueOf(0);
		double mainCost = financeSum.containsKey("��Ӫҵ��ɱ�") ? financeSum.get("��Ӫҵ��ɱ�") : Double.valueOf(0);
		double taxOrOthers = financeSum.containsKey("��Ӫҵ��˰�𼰸���") ? financeSum.get("��Ӫҵ��˰�𼰸���") : Double.valueOf(0);
		double mainProfit = mainIncome + mainCost + taxOrOthers;
		double otherIncome = financeSum.containsKey("����ҵ������") ? financeSum.get("����ҵ������") : Double.valueOf(0);
		double operCost = financeSum.containsKey("Ӫҵ����") ? financeSum.get("Ӫҵ����") : Double.valueOf(0);
		double adminCost = financeSum.containsKey("�������") ? financeSum.get("�������") : Double.valueOf(0);
		double financeCost = financeSum.containsKey("�������") ? financeSum.get("�������") : Double.valueOf(0);
		double operProfit = mainProfit + otherIncome + operCost + adminCost + financeCost;
		double investIncome = financeSum.containsKey("Ͷ������") ? financeSum.get("Ͷ������") : Double.valueOf(0);
		double operExcludeIncome = financeSum.containsKey("Ӫҵ������") ? financeSum.get("Ӫҵ������") : Double.valueOf(0);
		double operExcludeCost = financeSum.containsKey("Ӫҵ��֧��") ? financeSum.get("Ӫҵ��֧��") : Double.valueOf(0);
		double totalProfit = operProfit + investIncome + operExcludeIncome + operExcludeCost;
		double incomeTax = financeSum.containsKey("����˰") ? financeSum.get("����˰") : Double.valueOf(0);
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
			List<Financereceivable> financereceivables) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(path));
		document.open();

		createProfitReport(document, financeSumMonth, financeSumYear);
		if ((!financereceivables.isEmpty()) || financereceivables.get(0) != null
				|| financereceivables.get(0).getId() != null) {
			createPendingReport(document, financereceivables);
		}
		document.close();
	}

	public void createPendingReport(Document document, List<Financereceivable> financereceivables)
			throws IOException, DocumentException {
		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(500);
		table.setLockedWidth(true);
		BorderEvent event = new BorderEvent();
		table.setTableEvent(event);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setSplitLate(false);
		BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
		// Font fontChinese = new Font(bfChinese,12,Font.NORMAL,Color.GREEN);
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, BaseColor.BLACK);
		Font fontRed = new Font(bfChinese, 12, Font.NORMAL, BaseColor.RED);
		PdfPCell cell = new PdfPCell(new Phrase(TEXT, fontChinese));
		cell.setBorder(Rectangle.NO_BORDER);

		document.add(new Phrase("\r\n\r\nʣ��Ӧ����\r\n��������:" + dftime.format(new Date()) + "\r\n", fontChinese));
		cell.setPhrase(new Phrase("��˾", fontChinese));
		table.addCell(cell);
		cell.setPhrase(new Phrase("��������", fontChinese));
		table.addCell(cell);
		cell.setPhrase(new Phrase("ʣ��Ӧ��", fontChinese));
		table.addCell(cell);
		for (int i = 0; i < financereceivables.size(); i++) {
			cell.setPhrase(new Phrase(financereceivables.get(i).getCustomer().getName(), fontChinese));
			table.addCell(cell);
			cell.setPhrase(new Phrase(String.valueOf(financereceivables.get(i).getTotalamount()), fontChinese));
			table.addCell(cell);
			cell.setPhrase(new Phrase(
					String.valueOf(financereceivables.get(i).getTotalamount() - financereceivables.get(i).getNowpay()),
					fontChinese));
			table.addCell(cell);
		}
		event.setRowCount(table.getRows().size());
		document.add(table);
	}

	public void createProfitReport(Document document, List<String> financeSumMonth, List<String> financeSumYear)
			throws IOException, DocumentException {
		List<String> list = new ArrayList<String>();
		list.add("һ����Ӫҵ������");
		list.add("   ��: ��Ӫҵ��ɱ�");
		list.add("          ��Ӫҵ��˰�𼰸���");
		list.add("������Ӫҵ������");
		list.add("   ��: ����ҵ������");
		list.add("   ��: Ӫҵ����");
		list.add("          �������");
		list.add("          �������");
		list.add("����Ӫҵ����");
		list.add("   ��: Ͷ������");
		list.add("          Ӫҵ������");
		list.add("   ��: Ӫҵ��֧��");
		list.add("�ġ������ܶ�");
		list.add("   ��: ����˰");
		list.add("�塢������");

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(500);
		table.setLockedWidth(true);
		BorderEvent event = new BorderEvent();
		table.setTableEvent(event);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setSplitLate(false);
		BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
		// Font fontChinese = new Font(bfChinese,12,Font.NORMAL,Color.GREEN);
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, BaseColor.BLACK);
		Font fontRed = new Font(bfChinese, 12, Font.NORMAL, BaseColor.RED);
		PdfPCell cell = new PdfPCell(new Phrase(TEXT, fontChinese));
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
		document.add(new Phrase(dateInfo[0] + month + "����\r\n�Ʊ�����:" + dftime.format(new Date()) + "\r\n", fontChinese));
		cell.setPhrase(new Phrase("��Ŀ", fontChinese));
		table.addCell(cell);
		cell.setPhrase(new Phrase("������", fontChinese));
		table.addCell(cell);
		cell.setPhrase(new Phrase("�����ۼ���", fontChinese));
		table.addCell(cell);
		for (int i = 0; i < list.size(); i++) {
			cell.setPhrase(new Phrase(list.get(i), fontChinese));
			table.addCell(cell);
			cell.setPhrase((new Phrase(financeSumMonth.get(i),
					financeSumMonth.get(i).startsWith("-") ? fontRed : fontChinese)));
			table.addCell(cell);
			cell.setPhrase(
					(new Phrase(financeSumYear.get(i), financeSumYear.get(i).startsWith("-") ? fontRed : fontChinese)));
			table.addCell(cell);
		}
		event.setRowCount(table.getRows().size());
		document.add(table);

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
		BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
		// Font fontChinese = new Font(bfChinese,12,Font.NORMAL,Color.GREEN);
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, BaseColor.BLACK);
		PdfPCell cell = new PdfPCell(new Phrase(TEXT, fontChinese));
		cell.setBorder(Rectangle.NO_BORDER);
		for (int i = 0; i < 60;) {
			table.addCell("Cell " + (++i));
			table.addCell(cell);
		}
		event.setRowCount(table.getRows().size());
		document.add(table);
		document.close();
	}

	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
	private static SimpleDateFormat dftime = new SimpleDateFormat("yyyy-MM-dd");

	static {
		Properties prop = new Properties();
		System.out.println(PDFReport.class.getClassLoader().getResource("config.properties").getPath());
		try {
			// ��ȡ�����ļ�a.properties
			InputStream in = PDFReport.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(in); /// ���������б�
			DEST = prop.getProperty("report");
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}