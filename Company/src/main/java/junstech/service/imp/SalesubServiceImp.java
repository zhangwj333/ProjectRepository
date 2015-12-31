package junstech.service.imp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.SalesubMapper;
import junstech.model.Salesub;
import junstech.service.SalesubService;

@Transactional
@Service("salesubService")
public class SalesubServiceImp implements SalesubService {

	SalesubMapper salesubMapper;

	public SalesubMapper getSalesubMapper() {
		return salesubMapper;
	}

	@Autowired
	public void setSalesubMapper(SalesubMapper salesubMapper) {
		this.salesubMapper = salesubMapper;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Salesub selectSalesub(long id) throws Exception {
		return salesubMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<Salesub> selectSalesubs(long id) throws Exception {
		return salesubMapper.selectBySaleId(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createSalesub(List<Salesub> list) throws Exception {
		for (Salesub salesub : list) {
			if(salesub.getGoodid() == null){
				continue;
			}
			salesubMapper.insert(salesub);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editSalesub(Salesub salesub) throws Exception {
		salesubMapper.updateByPrimaryKey(salesub);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteSalesub(long id) throws Exception {
		salesubMapper.deleteByPrimaryKey(id);
	}

}
