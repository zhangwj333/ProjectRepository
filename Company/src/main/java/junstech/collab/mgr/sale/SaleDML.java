package junstech.collab.mgr.sale;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import junstech.collab.BaseController;
import junstech.exception.BusinessException;
import junstech.model.Purchase;
import junstech.model.Sale;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.CustomerService;
import junstech.service.GoodService;
import junstech.service.ProductService;
import junstech.service.SaleService;
import junstech.service.SupplierService;
import junstech.util.MetaData;

@Controller
public class SaleDML extends BaseController{

	public SaleDML() {
	}

	SaleService saleService;

	ProductService productService;

	CustomerService customerService;

	public CustomerService getCustomerService() {
		return customerService;
	}

	@Autowired
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public ProductService getProductService() {
		return productService;
	}

	@Autowired
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public SaleService getSaleService() {
		return saleService;
	}

	@Autowired
	public void setSaleService(SaleService saleService) {
		this.saleService = saleService;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// true:���������ֵ��false:����Ϊ��ֵ
	}

	@RequestMapping(value = "/querySales")
	public ModelAndView querys(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		return prepareView(mv, id, key, startdate, enddate, page, size, session);
	}

	@RequestMapping(value = "/querySale", method = RequestMethod.GET)
	public ModelAndView query(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Sale sale = saleService.selectSale(id);
		if (sale == null) {
			throw new BusinessException();
		}
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("salemanid", "销售员"));
		tablepropertys.add(new TableProperty("customerid", "客户"));
		tablepropertys.add(new TableProperty("saletime", "开单时间"));
		tablepropertys.add(new TableProperty("operman", "操作人"));
		tablepropertys.add(new TableProperty("total", "销售总价"));
		tablepropertys.add(new TableProperty("status", "状态"));
		tablepropertys.add(new TableProperty("note", "说明"));
		mv.addObject("tablepropertys", tablepropertys);
		List<TableProperty> tablesubpropertys = new ArrayList<TableProperty>();
		tablesubpropertys.add(new TableProperty("goodid", "商品"));
		tablesubpropertys.add(new TableProperty("price", "价格"));
		tablesubpropertys.add(new TableProperty("goodqty", "数量(吨)"));
		tablesubpropertys.add(new TableProperty("opertime", "送货时间"));
		tablesubpropertys.add(new TableProperty("verification", "是否核销"));
		mv.addObject("tablesubpropertys", tablesubpropertys);
		mv.addObject("tableline", sale);
		mv.addObject("tablesublines", sale.getSalesubs());
		mv.setViewName("saleShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editSale", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Sale sale = saleService.selectSale(id);
		if (sale == null) {
			throw new BusinessException();
		}
		sale.setNote(sale.getNote().replaceAll("<br/>", "\r\n"));
		List<String> statusOptions = new ArrayList<String>();
		statusOptions.add("�¿���");
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("salemanid", "销售员"));
		tablepropertys.add(new TableProperty("customerid", "客户"));
		tablepropertys.add(new TableProperty("total", "销售总价"));
		tablepropertys.add(new TableProperty("status", "状态"));
		tablepropertys.add(new TableProperty("note", "说明"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("customers", customerService.selectAllCustomers());
		mv.addObject("products", productService.selectAllProducts());
		mv.addObject("tableline", sale);
		mv.addObject("salesubs", sale.getSalesubs());
		mv.addObject("statusOptions", statusOptions);
		mv.addObject("action", "editSaleProcess");
		mv.addObject("modelAttribute", "Sale");
		mv.setViewName("saleEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editSaleProcess")
	public ModelAndView editProcess(@ModelAttribute Sale sale, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		String errorMsg = null;
		try {
			if (sale.getSalesubs() == null) {
				errorMsg = "销售单没有商品，请重新创建订单.";
				throw new BusinessException();
			}
			User user = (User) session.getAttribute("user");
			sale.setOperman(user.getId());
			sale.setNote(sale.getNote().replaceAll("\r\n", "<br/>"));
			saleService.editSale(sale);
			mv.addObject("message", "更新订单成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", "更新失败." + errorMsg);
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createSale")
	public ModelAndView create(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("salemanid", "销售员"));
		tablepropertys.add(new TableProperty("customerid", "客户"));
		tablepropertys.add(new TableProperty("total", "销售总价"));
		tablepropertys.add(new TableProperty("status", "状态"));
		tablepropertys.add(new TableProperty("note", "说明"));
		Sale sale = new Sale();
		List<String> statusOptions = new ArrayList<String>();
		statusOptions.add("新开单");
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", sale);
		mv.addObject("customers", customerService.selectAllCustomers());
		mv.addObject("products", productService.selectAllProducts());
		mv.addObject("statusOptions", statusOptions);
		mv.addObject("action", "createSaleProcess");
		mv.addObject("modelAttribute", "sale");
		mv.setViewName("saleCreate");

		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createSaleProcess")
	public ModelAndView createProcess(@ModelAttribute Sale sale, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		String errorMsg = null;
		try {
			if (sale.getSalesubs() == null) {
				errorMsg = "销售单没有商品，请重新创建订单.";
				throw new BusinessException();
			}
			User user = (User) session.getAttribute("user");
			sale.setSaletime(new Date());
			sale.setOperman(user.getId());
			if(!sale.getNote().trim().isEmpty()){
				sale.setNote(sale.getNote().concat("<br/>"));
			}
			else{
				sale.setNote("");
			}
			sale.setNote(sale.getNote().concat(df.format(new Date()) + ": 新开单"));
			saleService.createSale(sale);
			mv.addObject("message", "新建采购订单成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			if (errorMsg == null) {
				mv.addObject("message", "更新失败.");
				e.printStackTrace();
			} else {
				mv.addObject("message", "更新失败." + errorMsg);
			}
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/deleteSale")
	public ModelAndView deleteProcess(@RequestParam("id") String id, @RequestParam("key") String key, //
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate, //
			@RequestParam("page") int page, @RequestParam("size") int size, //
			HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			saleService.deleteSale(Long.parseLong(tempid));
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
			return prepareView(mv, id.split(",", 2)[1], key, startdate, enddate, page, size, session);
		} catch (Exception e) {
			mv.addObject("message", "删除失败，请重新操作!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.setNoteTitle, "结果");
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
			mv.setViewName("complete");
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
			return this.outputView(session, mv);
		}
	}

	@RequestMapping(value = "/submitSale")
	public ModelAndView submitProcess(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			Sale sale = saleService.selectSale(Long.parseLong(tempid));
			User user = (User) session.getAttribute("user");
			sale.setOperman(user.getId());
			sale.setStatus("待确认");
			sale.setNote(sale.getNote().replaceAll("\r\n", "<br/>"));
			saleService.editSale(sale);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
			return prepareView(mv, id.split(",", 2)[1], key, startdate, enddate, page, size, session);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("message", "更新失败，请重试!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.setNoteTitle, "结果");
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
			mv.setViewName("complete");
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
			return this.outputView(session, mv);
		}
	}

	public ModelAndView prepareView(//
			ModelAndView mv, //
			String id, String key, //
			String startdate, String enddate, //
			int page, int size, HttpSession session) throws Exception {
		long uid = 0;
		if (!id.isEmpty()) {
			uid = Long.parseLong(id);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", uid);
		map.put("key", key);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("prev", (page - 1) * size);
		map.put("next", size);
		List<Sale> sales = saleService.selectSales(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("salemanid", "销售员"));
		tablepropertys.add(new TableProperty("customerid", "客户"));
		tablepropertys.add(new TableProperty("saletime", "开单时间"));
		tablepropertys.add(new TableProperty("operman", "操作人"));
		tablepropertys.add(new TableProperty("total", "销售总价"));
		tablepropertys.add(new TableProperty("status", "状态"));
		tablepropertys.add(new TableProperty("note", "说明"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", sales);
		mv.addObject("criteria", "Sale");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", "销售订单");
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("startdate", startdate));
		searchFactors.add(new TableProperty("enddate", enddate));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (sales.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "querySales");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
}
