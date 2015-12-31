package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Customer;

public interface CustomerService {
	
	public Customer selectCustomer(long id) throws Exception;
	
	public List<Customer> selectAllCustomers() throws Exception;
	
	public List<Customer> selectCustomers(Map<String, Object> map) throws Exception;

	public void createCustomer(Customer customer) throws Exception;
	
	public void editCustomer(Customer customer) throws Exception;
	
	public void deleteCustomer(long id) throws Exception;
}
