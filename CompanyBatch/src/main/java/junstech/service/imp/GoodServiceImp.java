package junstech.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.GoodMapper;
import junstech.model.Good;
import junstech.service.GoodService;

@Transactional
@Service("goodService")
public class GoodServiceImp implements GoodService{

	GoodMapper goodMapper;
		
	public GoodMapper getGoodMapper() {
		return goodMapper;
	}

	@Autowired
	public void setGoodMapper(GoodMapper goodMapper) {
		this.goodMapper = goodMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Good selectGood(int id) throws Exception {	
		return goodMapper.selectByPrimaryKey(id);
	}

	public List<Good> selectGoods() throws Exception {
		return goodMapper.selectAllGoods();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Good> selectGoods(Map<String, Object> map) throws Exception {
		return goodMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createGood(Good good) throws Exception {
		goodMapper.insert(good);	
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editGood(Good good) throws Exception {
		goodMapper.updateByPrimaryKey(good);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteGood(int id) throws Exception {
		goodMapper.deleteByPrimaryKey(id);
	}

	
}
