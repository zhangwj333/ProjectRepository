package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Supplier;

public interface SupplierService {
	
	public Supplier selectSupplier(int id) throws Exception;
	
	public List<Supplier> selectAllSuppliers() throws Exception;
	
	public List<Supplier> selectSuppliers(Map<String, Object> map) throws Exception;

	public void createSupplier(Supplier supplier) throws Exception;
	
	public void editSupplier(Supplier supplier) throws Exception;
	
	public void deleteSupplier(int id) throws Exception;
}
