package com.example.apiserver.mapper;

import com.example.apiserver.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(@Param("id") Integer id);

    @Select("SELECT * FROM users WHERE openid = #{openid}")
    User findByOpenid(@Param("openid") String openid);

    int insert(User user);
    
    int updateBalance(@Param("id") Integer id, @Param("amount") java.math.BigDecimal amount);
}
