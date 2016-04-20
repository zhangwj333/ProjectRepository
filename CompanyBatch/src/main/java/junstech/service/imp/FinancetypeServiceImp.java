package junstech.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.FinancetypeMapper;
import junstech.model.Financetype;
import junstech.service.FinancetypeService;

@Transactional
@Service("financetypeService")
public class FinancetypeServiceImp implements FinancetypeService{
	
	FinancetypeMapper financetypeMapper;
	
	public FinancetypeMapper getFinancetypeMapper() {
		return financetypeMapper;
	}

	@Autowired
	public void setFinancetypeMapper(FinancetypeMapper financetypeMapper) {
		this.financetypeMapper = financetypeMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Financetype selectFinancetype(int id) throws Exception {
		return financetypeMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Financetype> selectAllFinancetypes() throws Exception {
		return financetypeMapper.selectAll();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Financetype> selectFinancetypes(Map<String, Object> map) throws Exception {
		return financetypeMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createFinancetype(Financetype financetype) throws Exception {
		financetypeMapper.insert(financetype);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editFinancetype(Financetype financetype) throws Exception {
		financetypeMapper.updateByPrimaryKey(financetype);		
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteFinancetype(int id) throws Exception {
		financetypeMapper.deleteByPrimaryKey(id);		
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Financetype selectFinancetype(String name) throws Exception {		
		return financetypeMapper.selectByName(name);
	}

}
