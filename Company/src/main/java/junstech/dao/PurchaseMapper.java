package junstech.dao;

import java.util.List;
import java.util.Map;

import junstech.model.Purchase;

public interface PurchaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long purchaseid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase
     *
     * @mbggenerated
     */
    int insert(Purchase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase
     *
     * @mbggenerated
     */
    Purchase selectByPrimaryKey(Long purchaseid);

    List<Purchase> selectPurchasesByStatus(String status);
    
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase
     *
     * @mbggenerated
     */
    List<Purchase> selectAll();

    List<Purchase> selectByPage(Map<String, Object> map);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Purchase record);
}