package junstech.collab.mgr.purchase;

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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import junstech.collab.BaseController;
import junstech.model.Purchase;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.GoodService;
import junstech.service.PurchaseService;
import junstech.service.SupplierService;
import junstech.util.MetaData;
import junstech.util.LanguageUtil;

@Controller
public class PurchaseDML extends BaseController{

	public PurchaseDML() {
	}

	PurchaseService purchaseService;

	GoodService goodService;

	SupplierService supplierService;

	public GoodService getGoodService() {
		return goodService;
	}

	@Autowired
	public void setGoodService(GoodService goodService) {
		this.goodService = goodService;
	}

	public SupplierService getSuppliereService() {
		return supplierService;
	}

	@Autowired
	public void setSuppliereService(SupplierService suppliereService) {
		this.supplierService = suppliereService;
	}

	public PurchaseService getPurchaseService() {
		return purchaseService;
	}

	@Autowired
	public void setPurchaseService(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}

	@RequestMapping(value = "/queryPurchases")
	public ModelAndView querys(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		return prepareView(mv, id, key, startdate, enddate, page, size, session);
	}

	@RequestMapping(value = "/queryPurchase")
	public ModelAndView query(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Purchase purchase = purchaseService.selectPurchase(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("purchasename", LanguageUtil.getString("purchasename")));
		tablepropertys.add(new TableProperty("goodid", LanguageUtil.getString("goodid")));
		tablepropertys.add(new TableProperty("goodqty", LanguageUtil.getString("goodqty")));
		tablepropertys.add(new TableProperty("price", LanguageUtil.getString("price")));
		tablepropertys.add(new TableProperty("userid",LanguageUtil.getString("userid")));
		tablepropertys.add(new TableProperty("status", LanguageUtil.getString("status")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", purchase);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editPurchase")
	public ModelAndView edit(@RequestParam("id") long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Purchase purchase = purchaseService.selectPurchase(id);
		purchase.setNote(purchase.getNote().replaceAll("<br/>", "\r\n"));
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("purchasename", LanguageUtil.getString("purchasename")));
		tablepropertys.add(new TableProperty("goodid", LanguageUtil.getString("goodid")));
		tablepropertys.add(new TableProperty("goodqty", LanguageUtil.getString("goodqty")));
		tablepropertys.add(new TableProperty("price", LanguageUtil.getString("price")));
		tablepropertys.add(new TableProperty("status", LanguageUtil.getString("status")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));
		
		List<String> statusOptions = new ArrayList<String>();
		statusOptions.add(LanguageUtil.getString("statusNew"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", purchase);
		mv.addObject("goods", goodService.selectGoods());
		mv.addObject("statusOptions", statusOptions);
		mv.addObject("action", "editPurchaseProcess");
		mv.addObject("modelAttribute", "Purchase");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editPurchaseProcess")
	public ModelAndView editProcess(@ModelAttribute Purchase purchase, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			User user = (User) session.getAttribute("user");
			purchase.setUserid(user.getId());
			purchase.setNote(purchase.getNote().replaceAll("\r\n", "<br/>"));
			purchaseService.editPurchase(purchase);
			mv.addObject("message", LanguageUtil.getString("updateSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", LanguageUtil.getString("updateFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, LanguageUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createPurchase")
	public ModelAndView create(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("goodid", LanguageUtil.getString("goodid")));
		tablepropertys.add(new TableProperty("goodqty", LanguageUtil.getString("goodqty")));
		tablepropertys.add(new TableProperty("price", LanguageUtil.getString("price")));
		tablepropertys.add(new TableProperty("status", LanguageUtil.getString("status")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));

		Purchase purchase = new Purchase();
		List<String> statusOptions = new ArrayList<String>();
		statusOptions.add(LanguageUtil.getString("statusNew"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", purchase);
		mv.addObject("goods", goodService.selectGoods());
		mv.addObject("statusOptions", statusOptions);
		mv.addObject("action", "createPurchaseProcess");
		mv.addObject("modelAttribute", "purchase");
		mv.setViewName("genCreate");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createPurchaseProcess")
	public ModelAndView createProcess(@ModelAttribute Purchase purchase, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			User user = (User) session.getAttribute("user");
			purchase.setUserid(user.getId());
			purchase.setPurchasename(purchase.getGoodid() + orderName.format(new Date()));
			if(!purchase.getNote().trim().isEmpty()){
				purchase.setNote(purchase.getNote().concat("<br/>"));
			}
			else{
				purchase.setNote("");
			}
			purchase.setNote(purchase.getNote().concat(df.format(new Date()) + ": "+LanguageUtil.getString("statusNew")));
			purchase.setNote(purchase.getNote().replaceAll("\r\n", "<br/>"));
			purchaseService.createPurchase(purchase);
			mv.addObject("message", LanguageUtil.getString("createSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", LanguageUtil.getString("createFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, LanguageUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/deletePurchase")
	public ModelAndView deleteProcess(@RequestParam("id") String id, @RequestParam("key") String key, //
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate, //
			@RequestParam("page") int page, @RequestParam("size") int size, //
			HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			purchaseService.deletePurchase(Long.parseLong(tempid));
			return prepareView(mv, id.split(",", 2)[1], key, startdate, enddate, page, size, session);
		} catch (Exception e) {
			mv.addObject("message", LanguageUtil.getString("deleteFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.setNoteTitle, LanguageUtil.getString("title"));
			mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
			mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
			mv.setViewName("complete");
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
			return this.outputView(session, mv);
		}
	}

	@RequestMapping(value = "/submitPurchase")
	public ModelAndView submitProcess(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			Purchase purchase = purchaseService.selectPurchase(Long.parseLong(tempid));
			User user = (User) session.getAttribute("user");
			purchase.setUserid(user.getId());
			purchase.setStatus(LanguageUtil.getString("statusPendingVerification"));
			purchase.setNote(purchase.getNote().replaceAll("\r\n", "<br/>"));
			purchaseService.editPurchase(purchase);
			return prepareView(mv, id.split(",", 2)[1], key, startdate, enddate, page, size, session);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("message", LanguageUtil.getString("submitFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.setNoteTitle, LanguageUtil.getString("title"));
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
		List<Purchase> purchases = purchaseService.selectPurchases(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("purchasename", LanguageUtil.getString("purchasename")));
		tablepropertys.add(new TableProperty("goodid", LanguageUtil.getString("goodid")));
		tablepropertys.add(new TableProperty("goodqty", LanguageUtil.getString("goodqty")));
		tablepropertys.add(new TableProperty("price", LanguageUtil.getString("price")));
		tablepropertys.add(new TableProperty("purchasedate", LanguageUtil.getString("purchasedate")));
		tablepropertys.add(new TableProperty("userid",LanguageUtil.getString("userid")));
		tablepropertys.add(new TableProperty("status", LanguageUtil.getString("status")));
		tablepropertys.add(new TableProperty("note", LanguageUtil.getString("note")));

		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", purchases);
		mv.addObject("criteria", "Purchase");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", LanguageUtil.getString("purchaseTitle"));
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("startdate", startdate));
		searchFactors.add(new TableProperty("enddate", enddate));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (purchases.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "queryPurchases");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	private SimpleDateFormat orderName = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
}
