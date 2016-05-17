package junstech.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.SaleMapper;
import junstech.model.Sale;
import junstech.model.Salesub;
import junstech.service.FinancereceivableService;
import junstech.service.InventoryService;
import junstech.service.SaleService;
import junstech.service.SalesubService;

@Transactional
@Service("saleService")
public class SaleServiceImp implements SaleService{

	SaleMapper saleMapper;
		
	SalesubService salesubService;
	
	InventoryService inventoryService;
	
	FinancereceivableService financereceivableService;
	
	public FinancereceivableService getFinancereceivableService() {
		return financereceivableService;
	}

	@Autowired
	public void setFinancereceivableService(FinancereceivableService financereceivableService) {
		this.financereceivableService = financereceivableService;
	}

	public InventoryService getInventoryService() {
		return inventoryService;
	}

	@Autowired
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	public SalesubService getSalesubService() {
		return salesubService;
	}

	@Autowired
	public void setSalesubService(SalesubService salesubService) {
		this.salesubService = salesubService;
	}

	public SaleMapper getSaleMapper() {
		return saleMapper;
	}

	@Autowired
	public void setSaleMapper(SaleMapper saleMapper) {
		this.saleMapper = saleMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Sale selectSale(long id) throws Exception {	
		return saleMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Sale> selectSales(Map<String, Object> map) throws Exception {
		return saleMapper.selectByPage(map);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Sale> selectSalesByStatus(String status) throws Exception{
		return saleMapper.selectByStatus(status);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createSale(Sale sale) throws Exception {
		saleMapper.insert(sale);
		for(int i=0;i <sale.getSalesubs().size();i++){
			sale.getSalesubs().get(i).setSaleid(sale.getId());
		}
		salesubService.createSalesub(sale.getSalesubs());
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editSale(Sale sale) throws Exception {
		saleMapper.updateByPrimaryKey(sale);
		salesubService.deleteSalesub(sale.getId());
		for(int i=0;i <sale.getSalesubs().size();i++){
			if(sale.getSalesubs().get(i).getGoodid() == null){
				continue;
			}
			sale.getSalesubs().get(i).setSaleid(sale.getId());
		}
		salesubService.createSalesub(sale.getSalesubs());		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteSale(long id) throws Exception {
		saleMapper.deleteByPrimaryKey(id);
		salesubService.deleteSalesub(id);
		inventoryService.deleteInventoryByActionId("sale" + id);
		financereceivableService.deleteFinancereceivable(id);
	}

	
}
