package com.example.apiserver.mapper;

import com.example.apiserver.entity.ServiceItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ServiceItemMapper {
    @Select("SELECT * FROM service_items WHERE id = #{id}")
    ServiceItem findById(@Param("id") Integer id);

    @Select("SELECT * FROM service_items WHERE status = #{status}")
    List<ServiceItem> findByStatus(@Param("status") String status);

    int insert(ServiceItem serviceItem);
    
    int update(ServiceItem serviceItem);
}
