package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Financetype;

public interface FinancetypeService {
	
	public Financetype selectFinancetype(int id) throws Exception;
	
	public Financetype selectFinancetype(String name) throws Exception;
	
	public List<Financetype> selectAllFinancetypes() throws Exception;
	
	public List<Financetype> selectFinancetypes(Map<String, Object> map) throws Exception;

	public void createFinancetype(Financetype financetype) throws Exception;
	
	public void editFinancetype(Financetype financetype) throws Exception;
	
	public void deleteFinancetype(int id) throws Exception;
}
