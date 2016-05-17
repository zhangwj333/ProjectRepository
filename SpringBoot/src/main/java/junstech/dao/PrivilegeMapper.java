package junstech.dao;

import java.util.List;
import junstech.model.Privilege;

public interface PrivilegeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table privilege
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table privilege
     *
     * @mbggenerated
     */
    int insert(Privilege record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table privilege
     *
     * @mbggenerated
     */
    Privilege selectByPrimaryKey(Long key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table privilege
     *
     * @mbggenerated
     */
    List<Privilege> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table privilege
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Privilege record);
}