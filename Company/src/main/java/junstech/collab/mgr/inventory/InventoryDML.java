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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import junstech.collab.BaseController;
import junstech.model.Inventory;
import junstech.model.Ledger;
import junstech.model.Product;
import junstech.model.Purchase;
import junstech.model.TableProperty;
import junstech.model.User;
import junstech.service.InventoryService;
import junstech.service.ProductService;
import junstech.service.InventoryService;
import junstech.util.FileUtil;
import junstech.util.MetaData;
import junstech.util.RedisUtil;

@Controller
public class InventoryDML extends BaseController{

	public InventoryDML() {
	}
	
	ProductService productService;
	
	InventoryService inventoryService;
	
	public ProductService getProductService() {
		return productService;
	}

	@Autowired
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public InventoryService getInventoryService() {
		return inventoryService;
	}

	@Autowired
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// true:���������ֵ��false:����Ϊ��ֵ
	}
	
	@RequestMapping(value = "/querySummaryInventorys")
	public ModelAndView queryInventorys(@RequestParam("id") String id, @RequestParam("key") String key,
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
		List<Inventory> inventorys = inventoryService.selectSummary(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", RedisUtil.getString("id")));
		tablepropertys.add(new TableProperty("goodsortid", RedisUtil.getString("goodsortid")));
		tablepropertys.add(new TableProperty("type", RedisUtil.getString("type")));
		tablepropertys.add(new TableProperty("inventoryqty", RedisUtil.getString("inventoryqty")));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", inventorys);
		mv.addObject("criteria", "SummaryInventory");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", RedisUtil.getString("inventoryTitle"));
		mv.addObject("showoper", "no");
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (inventorys.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "querySummaryInventorys");
		mv.setViewName("query");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/queryInventorys")
	public ModelAndView manageInventorys(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		return prepareView(mv, id, key, startdate, enddate, page, size, session);
	}
	
	@RequestMapping(value = "/queryInventory")
	public ModelAndView queryInventory(@RequestParam("id") Long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Inventory inventory = inventoryService.selectInventory(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", RedisUtil.getString("id")));
		tablepropertys.add(new TableProperty("actionid", RedisUtil.getString("actionid")));
		tablepropertys.add(new TableProperty("goodid", RedisUtil.getString("goodid")));
		tablepropertys.add(new TableProperty("goodsortid", RedisUtil.getString("goodsortid")));
		tablepropertys.add(new TableProperty("type", RedisUtil.getString("type")));
		tablepropertys.add(new TableProperty("status", RedisUtil.getString("status")));
		tablepropertys.add(new TableProperty("price", RedisUtil.getString("price")));
		tablepropertys.add(new TableProperty("inventoryqty", RedisUtil.getString("inventoryqty")));
		
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", inventory);
		mv.setViewName("criteriaShow");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/queryInventoryProof")
	public ModelAndView queryInventoryProof(@RequestParam("id") Long id, HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		Inventory inventory = inventoryService.selectInventory(id);
		mv.addObject("proofPath", inventory.getProof());
		mv.addObject("title", RedisUtil.getString("inventoryTitle"));
		mv.setViewName("proof");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/editInventoryProof")
	public ModelAndView editInventoryProof(@RequestParam("id") Long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Inventory inventory = inventoryService.selectInventory(id);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", "ID"));
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", inventory);
		mv.addObject("action", "editInventoryProofProcess");
		mv.addObject("modelAttribute", "Inventory");
		mv.setViewName("proofEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}
	
	@RequestMapping(value = "/editInventoryProofProcess")
	public ModelAndView editInventoryProofProcess(@RequestParam("id") Long id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;      
	        // ����ļ���      
	        MultipartFile file = multipartRequest.getFile("img");  
	        String path = "/" + df.format(new Date()) + FileUtil.getFileExtension(file.getOriginalFilename());
	        FileUtil.save(file, MetaData.cargoPath + path);
	        Inventory inventory = inventoryService.selectInventory(id);
	        inventory.setProof("/cargo" + path);
	        inventoryService.editInventory(inventory);
	        mv.addObject("message", RedisUtil.getString("updateSuccess"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoSuccess);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		} catch (Exception e) {
			e.printStackTrace();
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
	
	@RequestMapping(value = "/editInventory")
	public ModelAndView editInventory(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		Inventory inventory = inventoryService.selectInventory(id);
		List<Product> types = productService.selectAllProducts();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("id", RedisUtil.getString("id")));
		tablepropertys.add(new TableProperty("goodid", RedisUtil.getString("goodid")));
		tablepropertys.add(new TableProperty("goodsortid", RedisUtil.getString("goodsortid")));
		tablepropertys.add(new TableProperty("status", RedisUtil.getString("status")));
		tablepropertys.add(new TableProperty("price", RedisUtil.getString("price")));
		tablepropertys.add(new TableProperty("inventoryqty", RedisUtil.getString("inventoryqty")));
		tablepropertys.add(new TableProperty("actionid", RedisUtil.getString("actionid")));
		
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", inventory);
		mv.addObject("types", types);
		mv.addObject("action", "editInventoryProcess");
		mv.addObject("modelAttribute", "Inventory");
		mv.setViewName("genEdit");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/editInventoryProcess")
	public ModelAndView editInventoryProcess(@ModelAttribute Inventory inventory, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			inventoryService.editInventory(inventory);
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
	
	@RequestMapping(value = "/createInventory")
	public ModelAndView createInventory(HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("actionid", RedisUtil.getString("actionid")));
		tablepropertys.add(new TableProperty("goodid", RedisUtil.getString("goodid")));
		tablepropertys.add(new TableProperty("goodsortid", RedisUtil.getString("goodsortid")));
		tablepropertys.add(new TableProperty("status", RedisUtil.getString("status")));
		tablepropertys.add(new TableProperty("price", RedisUtil.getString("price")));
		tablepropertys.add(new TableProperty("inventoryqty", RedisUtil.getString("inventoryqty")));
		
		Inventory inventory = new Inventory();
		List<Product> types = productService.selectAllProducts();
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tableline", inventory);
		mv.addObject("types", types);
		mv.addObject("action", "createInventoryProcess");
		mv.addObject("modelAttribute", "inventory");
		mv.setViewName("genCreate");
		mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
		return this.outputView(session, mv);
	}

	@RequestMapping(value = "/createInventoryProcess")
	public ModelAndView createInventoryProcess(@ModelAttribute Inventory Inventory, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			inventoryService.createInventory(Inventory);
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

	@RequestMapping(value = "/deleteInventory")
	public ModelAndView deleteInventoryProcess(@RequestParam("id") int id, HttpServletRequest request, HttpSession session)
			throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			inventoryService.deleteInventory(id);
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
	
	@RequestMapping(value = "/submitInventory")
	public ModelAndView submitProcess(@RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
			@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();

		try {
			String tempid = id.split("," , 2)[0];
			Inventory inventory = inventoryService.selectInventory(Long.parseLong(tempid));
			if(inventory.getActionid().contains("purchase")){
				inventory.setStatus(RedisUtil.getString("statusCompleteGoToInventory"));
				inventory.setType(RedisUtil.getString("spot"));
				inventory.setExecutedate(new Date());
			}else if (inventory.getActionid().contains("sale")){
				inventory.setStatus("statusCompleteShipPutFromInventory");			
			}
			inventoryService.editInventory(inventory);
			mv.addObject(MetaData.ProcessResult, MetaData.ProcessSuccess);
			return prepareView(mv, id.split("," , 2)[1], key, startdate, enddate, page, size, session);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("message", RedisUtil.getString("submitFail"));
			mv.addObject(MetaData.setNoteType, MetaData.cosmoDanger);
			mv.addObject(MetaData.setNoteTitle, RedisUtil.getString("title"));
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("key", key);
		map.put("prev",  (page - 1) * size);
		map.put("next",  size);
		List<Inventory> inventorys = inventoryService.selectInventorys(map);
		List<TableProperty> tablepropertys = new ArrayList<TableProperty>();
		List<TableProperty> searchFactors = new ArrayList<TableProperty>();
		tablepropertys.add(new TableProperty("actionid", RedisUtil.getString("actionid")));
		tablepropertys.add(new TableProperty("goodid", RedisUtil.getString("goodid")));
		tablepropertys.add(new TableProperty("goodsortid", RedisUtil.getString("goodsortid")));
		tablepropertys.add(new TableProperty("type", RedisUtil.getString("type")));
		tablepropertys.add(new TableProperty("status", RedisUtil.getString("status")));
		tablepropertys.add(new TableProperty("executedate", RedisUtil.getString("executedate")));
		tablepropertys.add(new TableProperty("price", RedisUtil.getString("price")));
		tablepropertys.add(new TableProperty("inventoryqty", RedisUtil.getString("inventoryqty")));
		
		mv.addObject("tablepropertys", tablepropertys);
		mv.addObject("tablelines", inventorys);
		mv.addObject("criteria", "Inventory");
		mv.addObject("page", page);
		mv.addObject("size", size);
		mv.addObject("title", RedisUtil.getString("inventoryTitle"));
		searchFactors.add(new TableProperty("id", id));
		searchFactors.add(new TableProperty("key", key));
		searchFactors.add(new TableProperty("startdate", startdate));
		searchFactors.add(new TableProperty("enddate", enddate));
		searchFactors.add(new TableProperty("page", page));
		searchFactors.add(new TableProperty("size", size));
		mv.addObject("searchFactors", searchFactors);
		if (inventorys.size() < size) {
			mv.addObject("lastpage", page);
		}
		mv.addObject("pagelink", "queryInventorys");
		mv.setViewName("query");
		
		return this.outputView(session, mv);
	}
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
}
