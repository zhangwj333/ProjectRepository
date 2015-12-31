package junstech.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.FinancereceivableMapper;
import junstech.model.Financereceivable;
import junstech.service.FinancereceivableService;

@Transactional
@Service("financereceivableService")
public class FinancereceivableServiceImp implements FinancereceivableService{

	FinancereceivableMapper financereceivableMapper;
		
	public FinancereceivableMapper getFinancereceivableMapper() {
		return financereceivableMapper;
	}

	@Autowired
	public void setFinancereceivableMapper(FinancereceivableMapper financereceivableMapper) {
		this.financereceivableMapper = financereceivableMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Financereceivable selectFinancereceivable(Long id) throws Exception {	
		return financereceivableMapper.selectByPrimaryKey(id);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Financereceivable> selectFinancereceivables(Map<String, Object> map) throws Exception {
		return financereceivableMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Financereceivable> selectSummary(Map<String, Object> map) throws Exception {
		return financereceivableMapper.selectSummaryByPage(map);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Financereceivable> selectAllByFields(Map<String, Object> map) throws Exception {
		return financereceivableMapper.selectAllByFields(map);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createFinancereceivable(Financereceivable financereceivable) throws Exception {
		financereceivableMapper.insert(financereceivable);	
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editFinancereceivable(Financereceivable financereceivable) throws Exception {
		financereceivableMapper.updateByPrimaryKey(financereceivable);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteFinancereceivable(Long id) throws Exception {
		financereceivableMapper.deleteByPrimaryKey(id);
	}
	
}
