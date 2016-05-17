package junstech.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.PaymentaccountMapper;
import junstech.model.Paymentaccount;
import junstech.service.PaymentaccountService;

@Transactional
@Service("paymentaccountService")
public class PaymentaccountServiceImp implements PaymentaccountService{
	
	PaymentaccountMapper paymentaccountMapper;
	
	public PaymentaccountMapper getPaymentaccountMapper() {
		return paymentaccountMapper;
	}

	@Autowired
	public void setPaymentaccountMapper(PaymentaccountMapper paymentaccountMapper) {
		this.paymentaccountMapper = paymentaccountMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Paymentaccount selectPaymentaccount(int id) throws Exception {
		return paymentaccountMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Paymentaccount> selectAllPaymentaccounts() throws Exception {
		return paymentaccountMapper.selectAll();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Paymentaccount> selectPaymentaccounts(Map<String, Object> map) throws Exception {
		return paymentaccountMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createPaymentaccount(Paymentaccount paymentaccount) throws Exception {
		paymentaccountMapper.insert(paymentaccount);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editPaymentaccount(Paymentaccount paymentaccount) throws Exception {
		paymentaccountMapper.updateByPrimaryKey(paymentaccount);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deletePaymentaccount(int id) throws Exception {
		paymentaccountMapper.deleteByPrimaryKey(id);		
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Paymentaccount selectPaymentaccount(String name) throws Exception {		
		return paymentaccountMapper.selectByName(name);
	}

}
