package junstech.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.LedgerMapper;
import junstech.model.Ledger;
import junstech.service.LedgerService;

@Transactional
@Service("ledgerService")
public class LedgerServiceImp implements LedgerService{

	LedgerMapper ledgerMapper;
		
	public LedgerMapper getLedgerMapper() {
		return ledgerMapper;
	}

	@Autowired
	public void setLedgerMapper(LedgerMapper ledgerMapper) {
		this.ledgerMapper = ledgerMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Ledger selectLedger(long id) throws Exception {	
		return ledgerMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Ledger> selectLedgersForBatch() throws Exception {
		return ledgerMapper.selectForVerifyBatch();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Ledger> selectLedgers(Map<String, Object> map) throws Exception {
		return ledgerMapper.selectByPage(map);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Ledger> selectLedgersByFields(Map<String, Object> map) throws Exception {
		return ledgerMapper.selectByPage(map);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Ledger> selectTotalByFields(Map<String, Object> map) throws Exception {
		return ledgerMapper.selectTotalByFields(map);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Ledger> selectSummary(Map<String, Object> map) throws Exception {
		return ledgerMapper.selectSummaryByPage(map);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Ledger selectLedger(int id, String type) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", type);
		return ledgerMapper.selectSummary(map);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createLedger(Ledger ledger) throws Exception {
		ledgerMapper.insert(ledger);	
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void editLedger(Ledger ledger) throws Exception {
		ledgerMapper.updateByPrimaryKey(ledger);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteLedger(long id) throws Exception {
		ledgerMapper.deleteByPrimaryKey(id);
	}

	
}
