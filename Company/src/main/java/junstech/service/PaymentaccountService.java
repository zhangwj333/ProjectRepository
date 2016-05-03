package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Paymentaccount;

public interface PaymentaccountService {
	
	public Paymentaccount selectPaymentaccount(int id) throws Exception;
	
	public Paymentaccount selectPaymentaccount(String name) throws Exception;
	
	public List<Paymentaccount> selectAllPaymentaccounts() throws Exception;
	
	public List<Paymentaccount> selectPaymentaccounts(Map<String, Object> map) throws Exception;

	public void createPaymentaccount(Paymentaccount paymentaccount) throws Exception;
	
	public void editPaymentaccount(Paymentaccount paymentaccount) throws Exception;
	
	public void deletePaymentaccount(int id) throws Exception;
}
