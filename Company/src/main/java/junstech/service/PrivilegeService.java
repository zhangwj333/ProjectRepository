package junstech.service;

import java.util.List;

import junstech.model.Privilege;
import junstech.model.User;



public interface PrivilegeService {
	public Privilege getPrivilege(long id) throws Exception;
	
	public List<Privilege> getAllPrivilege() throws Exception;
	
	public void createPrivilege(List<Privilege> privileges) throws Exception;
	
	public void updatePrivilege(List<Privilege> privileges) throws Exception;
	
	public void deletePrivilege(long id) throws Exception;
}
