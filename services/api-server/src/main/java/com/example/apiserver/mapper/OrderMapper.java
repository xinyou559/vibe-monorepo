package com.example.apiserver.mapper;

import com.example.apiserver.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order findById(@Param("id") Integer id);

    @Select("SELECT * FROM orders ORDER BY created_at DESC")
    java.util.List<Order> findAll();

    int insert(Order order);
    
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
}
