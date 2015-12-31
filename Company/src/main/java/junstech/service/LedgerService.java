package junstech.service;

import java.util.List;
import java.util.Map;

import junstech.model.Ledger;

public interface LedgerService {
	public Ledger selectLedger(long id) throws Exception;
	
	public List<Ledger> selectLedgersForBatch() throws Exception;
	
	public List<Ledger> selectLedgers(Map<String, Object> map) throws Exception;
	
	public List<Ledger> selectSummary(Map<String, Object> map) throws Exception;
	
	public List<Ledger> selectLedgersByFields(Map<String, Object> map) throws Exception;
	
	public List<Ledger> selectTotalByFields(Map<String, Object> map)throws Exception;
	
	public Ledger selectLedger(int id, String action) throws Exception;
	
	public void createLedger(Ledger ledger) throws Exception;
	
	public void editLedger(Ledger ledger) throws Exception;
	
	public void deleteLedger(long id) throws Exception;
	
}
