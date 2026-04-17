package com.example.apiserver.mapper;

import com.example.apiserver.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order findById(@Param("id") Integer id);

    int insert(Order order);
    
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
}
