package junstech.service.imp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.UserMapper;
import junstech.model.Privilege;
import junstech.model.User;
import junstech.service.PrivilegeService;
import junstech.service.UserService;
import junstech.util.AESEncryption;
import junstech.util.ENVConfig;

@Transactional
@Service("userService")
public class UserServiceImp implements UserService {
	private UserMapper userMapper;

	PrivilegeService privilegeService;
	
	
	public PrivilegeService getPrivilegeService() {
		return privilegeService;
	}

	@Autowired
	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}

	public UserMapper getUserMapper() {
		return userMapper;
	}

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public UserMapper getUserDao() {
		return userMapper;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public User getUser(long id) throws Exception {
		return userMapper.selectByPrimaryKey(id);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public User getUser(String username) throws Exception {
		return userMapper.selectByUserName(username);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public User getUserWithPrivilege(long id) throws Exception {
		return userMapper.selectByPrimaryKeyWithPrivilege(id);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public User getUserWithPrivilege(String username) throws Exception {
		return userMapper.selectByUserNameWithPrivilege(username);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public List<User> getAllUser() throws Exception {
		return userMapper.selectAll();
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public List<User> getAllUser(Map<String, Object> map) throws Exception {
		return userMapper.selectPage(map);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createUser(User user) throws Exception {
		long id = Long.parseLong(user.getPhone());
		user.setId(id);
		user.setPassword(AESEncryption.encrypt(user.getPassword(), ENVConfig.encryptKey));
		for (int i = 0; i < user.getPrivileges().size(); i++) {
			user.getPrivileges().get(i).setId(user.getId());
		}
		user.setCreateTime(new Date());
		userMapper.insert(user);
		privilegeService.createPrivilege(user.getPrivileges());

	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void updateUser(User user) throws Exception {
		user.setLastlogintime(new Date());
		user.setPassword(AESEncryption.encrypt(user.getPassword(), ENVConfig.encryptKey));
		userMapper.updateByPrimaryKey(user);
		privilegeService.updatePrivilege(user.getPrivileges());
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteUser(long id) throws Exception {
		userMapper.deleteByPrimaryKey(id);
		privilegeService.deletePrivilege(id);
	}
}
