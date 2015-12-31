package junstech.dao;

import java.util.List;
import java.util.Map;

import junstech.model.Financetype;
import junstech.model.Product;

public interface FinancetypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table financetype
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table financetype
     *
     * @mbggenerated
     */
    int insert(Financetype record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table financetype
     *
     * @mbggenerated
     */
    Financetype selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table financetype
     *
     * @mbggenerated
     */
    List<Financetype> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table supplier
     *
     * @mbggenerated
     */
    List<Financetype> selectByPage(Map<String, Object> map);
    
    Financetype selectByName(String name);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table financetype
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Financetype record);
}