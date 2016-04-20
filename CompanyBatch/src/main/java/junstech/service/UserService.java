package junstech.service;

import java.util.List;
import java.util.Map;
import java.util.Date;
import junstech.model.User;

public interface UserService {
	
	public User getUser(long id) throws Exception;
	
	public User getUser(String username) throws Exception;
	
	public User getUserWithPrivilege(long id) throws Exception;
	
	public User getUserWithPrivilege(String username) throws Exception;
	
	public List<User> getAllUser() throws Exception;
	
	public List<User> getAllUser(Map<String, Object> map) throws Exception;
	
	public void createUser(User user) throws Exception;
	
	public void updateUser(User user) throws Exception;
	
	public void deleteUser(long id) throws Exception;
}
