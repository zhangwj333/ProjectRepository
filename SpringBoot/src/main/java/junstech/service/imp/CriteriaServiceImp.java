package junstech.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import junstech.dao.CriteriaMapper;
import junstech.model.Criteria;
import junstech.service.CriteriaService;

@Service("criteriaService")
public class CriteriaServiceImp implements CriteriaService{
	private CriteriaMapper criteriaMapper;
	
	public CriteriaMapper getCriteriaMapper() {
		return criteriaMapper;
	}

	@Autowired
	public void setCriteriaMapper(CriteriaMapper criteriaMapper) {
		this.criteriaMapper = criteriaMapper;
	}

	public Criteria getCriteria(int id) {		
		return criteriaMapper.selectByPrimaryKey(id);
	}

	public List<Criteria> getAllCriteria() {
		return criteriaMapper.selectAll();
	}
	

}
