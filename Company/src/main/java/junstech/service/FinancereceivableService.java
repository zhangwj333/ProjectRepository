package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Financereceivable;

public interface FinancereceivableService {
	
	public Financereceivable selectFinancereceivable(Long id) throws Exception;
	
	public List<Financereceivable> selectFinancereceivables(Map<String, Object> map) throws Exception;
	
	public List<Financereceivable> selectSummary(Map<String, Object> map) throws Exception;
	
	public List<Financereceivable> selectAllByFields(Map<String, Object> map) throws Exception;
	
	public void createFinancereceivable(Financereceivable financereceivable) throws Exception;
	
	public void editFinancereceivable(Financereceivable financereceivable) throws Exception;
	
	public void deleteFinancereceivable(Long id) throws Exception;

}
