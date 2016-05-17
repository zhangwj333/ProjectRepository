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
import junstech.model.Good;
import junstech.model.Product;
import junstech.model.TableProperty;
import junstech.service.GoodService;
import junstech.service.ProductService;
import junstech.service.SupplierService;
import junstech.util.MetaData;
import junstech.util.LanguageUtil;

@Controller
public class GoodDML extends BaseController{

	public GoodDML() {
	}

	GoodService goodService;
	
	ProductService productService;
	
	SupplierService supplierService;
	
	public ProductService getProductService() {
		return productService;
	}

	@Autowired
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public GoodService getGoodService() {
		return goodService;
	}

	@Autowired
	public void setGoodService(GoodService goodService) {
		this.goodService = goodService;
	}

	public SupplierService getSupplierService() {
		return supplierService;
	}

	@Autowired
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}
	
	@RequestMapping(value = "/queryGoods")
	public ModelAndView queryUsers(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		int uid = 0;
		if(!id.isEmpty()){
			uid = Integer.parseInt(id);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", uid);
		map.put("key", key);
		map.put("prev",  (page - 1) * size);
		map.put("next",  size);
		List<Good> goods = goodService.selectGoods(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("goodname", LanguageUtil.getString("goodname")));
		tablepropertys.add(new TableProperty("goodsortid", LanguageUtil.getString("goodsortid")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", goods);
		mv.addObject("criteria", "Good");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", LanguageUtil.getString("goodname"));
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (goods.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "queryGoods");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/queryGood")
	public ModelAndView queryUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Good good = goodService.selectGood(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("goodname", LanguageUtil.getString("goodname")));
		tablepropertys.add(new TableProperty("goodsortid", LanguageUtil.getString("goodsortid")));mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", good);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editGood")
	public ModelAndView editUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Good good = goodService.selectGood(id);
		List<Product> types = productService.selectAllProducts();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", LanguageUtil.getString("id")));
		tablepropertys.add(new TableProperty("goodname", LanguageUtil.getString("goodname")));
		tablepropertys.add(new TableProperty("goodsortid", LanguageUtil.getString("goodsortid")));mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", good);
		mv.addObject("types", types);
		mv.addObject("action", "editGoodProcess");
		mv.addObject("modelAttribute", "Good");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editGoodProcess")
	public ModelAndView editUserProcess(@ModelAttribute Good good, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			goodService.editGood(good);
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
	
	@RequestMapping(value = "/createGood")
	public ModelAndView createUser(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("goodname", LanguageUtil.getString("goodname")));
		Good good = new Good();
		List<Product> types = productService.selectAllProducts();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", good);
		mv.addObject("types", types);
		mv.addObject("suppliers", supplierService.selectAllSuppliers());
		mv.addObject("action", "createGoodProcess");
		mv.addObject("modelAttribute", "good");
		mv.setViewName("goodCreate");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createGoodProcess")
	public ModelAndView createUserProcess(@ModelAttribute Good Good, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			goodService.createGood(Good);
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

	@RequestMapping(value = "/deleteGood")
	public ModelAndView deleteUserProcess(@RequestParam("id") String id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			goodService.deleteGood(Integer.parseInt(tempid));
			mv.addObject("message", LanguageUtil.getString("deleteSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", LanguageUtil.getString("deleteFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, LanguageUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
}
