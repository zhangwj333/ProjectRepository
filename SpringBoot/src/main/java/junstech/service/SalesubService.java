package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Salesub;

public interface SalesubService {
	public Salesub selectSalesub(long id) throws Exception;

	public void createSalesub(List<Salesub> list) throws Exception;
	
	public void editSalesub(Salesub salesub) throws Exception;
	
	public void deleteSalesub(long id) throws Exception;
}
