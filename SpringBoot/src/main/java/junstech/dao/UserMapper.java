package junstech.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import junstech.model.User;

public interface UserMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user
	 *
	 * @mbggenerated
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user
	 *
	 * @mbggenerated
	 */
	int insert(User record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user
	 *
	 * @mbggenerated
	 */
	User selectByPrimaryKey(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user
	 *
	 * @mbggenerated
	 */
	User selectByUserName(String username);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user
	 *
	 * @mbggenerated
	 */
	User selectByPrimaryKeyWithPrivilege(Long id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user
	 *
	 * @mbggenerated
	 */
	User selectByUserNameWithPrivilege(String username);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user
	 *
	 * @mbggenerated
	 */
	List<User> selectAll();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user
	 *
	 * @mbggenerated
	 */
	List<User> selectPage(Map<String, Object> map);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user
	 *
	 * @mbggenerated
	 */
	int updateByPrimaryKey(User record);
}