package junstech.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import junstech.dao.PrivilegeMapper;
import junstech.model.Privilege;
import junstech.service.PrivilegeService;

@Transactional
@Service("privilegeService")
public class PrivilegeServiceImp implements PrivilegeService {

	private PrivilegeMapper privilegeMapper;

	public PrivilegeMapper getPrivilegeMapper() {
		return privilegeMapper;
	}

	@Autowired
	public void setPrivilegeMapper(PrivilegeMapper privilegeMapper) {
		this.privilegeMapper = privilegeMapper;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Privilege getPrivilege(long id) throws Exception {
		return privilegeMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<Privilege> getAllPrivilege() throws Exception {
		return privilegeMapper.selectAll();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void createPrivilege(List<Privilege> privileges) throws Exception {
		for (Privilege privilege : privileges) {
			privilegeMapper.insert(privilege);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void updatePrivilege(List<Privilege> privileges) throws Exception {
		for (Privilege privilege : privileges) {
			if (privilege.getKey() == null) {
				privilegeMapper.insert(privilege);
			} else {
				privilegeMapper.updateByPrimaryKey(privilege);
			}
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deletePrivilege(long id) throws Exception {
		privilegeMapper.deleteByPrimaryKey(Long.parseLong(String.format("%d", id)));
	}
}
