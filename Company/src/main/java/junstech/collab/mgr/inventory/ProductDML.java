package junstech.collab.mgr.inventory;

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
import junstech.model.Criteria;
import junstech.model.Privilege;
import junstech.model.Product;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.CriteriaService;
import junstech.service.GoodService;
import junstech.service.PrivilegeService;
import junstech.service.ProductService;
import junstech.service.UserService;
import junstech.util.AESEncryption;
import junstech.util.ENVConfig;
import junstech.util.MetaData;
import junstech.util.RedisUtil;

@Controller
public class ProductDML extends BaseController{

	public ProductDML() {
	}
	
	ProductService productService;

	public ProductService getProductService() {
		return productService;
	}

	@Autowired
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	@RequestMapping(value = "/queryProducts")
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
		List<Product> products = productService.selectProducts(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", RedisUtil.getString("id")));
		tablepropertys.add(new TableProperty("goodname", RedisUtil.getString("goodname")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", products);
		mv.addObject("criteria", "Product");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", RedisUtil.getString("goodTitle"));
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (products.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "queryProducts");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/queryProduct")
	public ModelAndView queryUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Product product = productService.selectProduct(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", RedisUtil.getString("id")));
		tablepropertys.add(new TableProperty("goodname", RedisUtil.getString("goodname")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", product);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editProduct")
	public ModelAndView editUser(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Product product = productService.selectProduct(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", RedisUtil.getString("id")));
		tablepropertys.add(new TableProperty("goodname", RedisUtil.getString("goodname")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", product);
		mv.addObject("action", "editProductProcess");
		mv.addObject("modelAttribute", "product");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editProductProcess")
	public ModelAndView editUserProcess(@ModelAttribute Product product, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			productService.editProduct(product);
			mv.addObject("message", RedisUtil.getString("updateSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", RedisUtil.getString("updateFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, RedisUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/createProduct")
	public ModelAndView createUser(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("goodname", RedisUtil.getString("goodname")));
		Product product = new Product();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", product);
		mv.addObject("action", "createProductProcess");
		mv.addObject("modelAttribute", "product");
		mv.setViewName("genCreate");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createProductProcess")
	public ModelAndView createUserProcess(@ModelAttribute Product product, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			productService.createProduct(product);
			mv.addObject("message", RedisUtil.getString("createSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", RedisUtil.getString("createFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, RedisUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/deleteProduct")
	public ModelAndView deleteUserProcess(@RequestParam("id") String id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split(",", 2)[0];
			productService.deleteProduct(Integer.parseInt(tempid));
			mv.addObject("message", RedisUtil.getString("deleteSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			mv.addObject("message", RedisUtil.getString("deleteFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessFail);
		}

		mv.addObject(MetaData.setNoteTitle, RedisUtil.getString("title"));
		mv.addObject(MetaData.completeReturnPage, "redirect.htm?view=content");
		mv.addObject(MetaData.setTargetFrame, MetaData.setTargetAsContentFrame);
		mv.setViewName("complete");
		return this.outputView(session, mv);
	}
}
