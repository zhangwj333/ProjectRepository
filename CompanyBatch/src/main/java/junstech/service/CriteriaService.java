package junstech.service;

import java.util.List;

import junstech.model.Criteria;

public interface CriteriaService {
	public Criteria getCriteria(int id);
	
	public List<Criteria> getAllCriteria();
}
