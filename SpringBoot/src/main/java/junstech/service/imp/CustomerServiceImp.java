package junstech.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.CustomerMapper;
import junstech.model.Customer;
import junstech.service.CustomerService;

@Transactional
@Service("customerService")
public class CustomerServiceImp implements CustomerService{
	
	CustomerMapper customerMapper;
	
	public CustomerMapper getCustomerMapper() {
		return customerMapper;
	}

	@Autowired
	public void setCustomerMapper(CustomerMapper customerMapper) {
		this.customerMapper = customerMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Customer selectCustomer(long id) throws Exception {
		return customerMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Customer> selectAllCustomers() throws Exception {
		return customerMapper.selectAll();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Customer> selectCustomers(Map<String, Object> map) throws Exception {
		return customerMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createCustomer(Customer customer) throws Exception {
		customerMapper.insert(customer);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editCustomer(Customer customer) throws Exception {
		customerMapper.updateByPrimaryKey(customer);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteCustomer(long id) throws Exception {
		customerMapper.deleteByPrimaryKey(id);		
	}
	

}
