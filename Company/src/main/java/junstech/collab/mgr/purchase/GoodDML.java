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


import junstech.model.Good;
import junstech.model.Product;
import junstech.model.TableProperty;
import junstech.service.GoodService;
import junstech.service.ProductService;
import junstech.service.SupplierService;
import junstech.util.MetaData;

@Controller
public class GoodDML {

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
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("goodname", "采购商品"));
		tablepropertys.add(new TableProperty("goodsortid", "对应公司商品"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", goods);
		mv.addObject("criteria", "Good");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", "货物");
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

		return mv;
	}
	
	@RequestMapping(value = "/queryGood", method = RequestMethod.GET)
	public ModelAndView queryUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Good good = goodService.selectGood(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("goodname", "采购商品"));
		tablepropertys.add(new TableProperty("goodsortid", "对应公司商品"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", good);
		mv.setViewName("criteriaShow");

		return mv;
	}

	@RequestMapping(value = "/editGood", method = RequestMethod.GET)
	public ModelAndView editUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Good good = goodService.selectGood(id);
		List<Product> types = productService.selectAllProducts();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		tablepropertys.add(new TableProperty("goodname", "采购商品"));
		tablepropertys.add(new TableProperty("goodsortid", "对应公司商品"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", good);
		mv.addObject("types", types);
		mv.addObject("action", "editGoodProcess");
		mv.addObject("modelAttribute", "Good");
		mv.setViewName("genEdit");

		return mv;
	}

	@RequestMapping(value = "/editGoodProcess", method = RequestMethod.POST)
	public ModelAndView editUserProcess(@ModelAttribute Good good, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			goodService.editGood(good);
			mv.addObject("message", "更新商品成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
		} catch (Exception e) {
			mv.addObject("message", "更新失败，请重试!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return mv;
	}
	
	@RequestMapping(value = "/createGood")
	public ModelAndView createUser(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("goodname", "采购商品"));
		Good good = new Good();
		List<Product> types = productService.selectAllProducts();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", good);
		mv.addObject("types", types);
		mv.addObject("suppliers", supplierService.selectAllSuppliers());
		mv.addObject("action", "createGoodProcess");
		mv.addObject("modelAttribute", "good");
		mv.setViewName("goodCreate");

		return mv;
	}

	@RequestMapping(value = "/createGoodProcess", method = RequestMethod.POST)
	public ModelAndView createUserProcess(@ModelAttribute Good Good, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			goodService.createGood(Good);
			mv.addObject("message", "新建商品成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
		} catch (Exception e) {
			mv.addObject("message", "创建失败，请重新操作!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return mv;
	}

	@RequestMapping(value = "/deleteGood", method = RequestMethod.GET)
	public ModelAndView deleteUserProcess(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			goodService.deleteGood(id);
			mv.addObject("message", "删除商品成功");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
		} catch (Exception e) {
			mv.addObject("message", "删除失败，请重新操作!");
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
		}

		mv.addObject(MetaData.setNoteTitle, "结果");
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return mv;
	}
}
