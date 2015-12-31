package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Good;

public interface GoodService {
	public Good selectGood(int id) throws Exception;
	
	public List<Good> selectGoods() throws Exception;
	
	public List<Good> selectGoods(Map<String, Object> map) throws Exception;

	public void createGood(Good good) throws Exception;
	
	public void editGood(Good good) throws Exception;
	
	public void deleteGood(int id) throws Exception;
}
