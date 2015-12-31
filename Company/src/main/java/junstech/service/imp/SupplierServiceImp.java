package junstech.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.SupplierMapper;
import junstech.model.Supplier;
import junstech.service.SupplierService;

@Transactional
@Service("supplierService")
public class SupplierServiceImp implements SupplierService{
	
	SupplierMapper supplierMapper;
	
	public SupplierMapper getSupplierMapper() {
		return supplierMapper;
	}

	@Autowired
	public void setSupplierMapper(SupplierMapper supplierMapper) {
		this.supplierMapper = supplierMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Supplier selectSupplier(int id) throws Exception {
		return supplierMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Supplier> selectAllSuppliers() throws Exception {
		return supplierMapper.selectAll();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Supplier> selectSuppliers(Map<String, Object> map) throws Exception {
		return supplierMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createSupplier(Supplier supplier) throws Exception {
		supplierMapper.insert(supplier);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editSupplier(Supplier supplier) throws Exception {
		supplierMapper.updateByPrimaryKey(supplier);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteSupplier(int id) throws Exception {
		supplierMapper.deleteByPrimaryKey(id);		
	}
	

}
