package com.example.apiserver.mapper;

import com.example.apiserver.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TransactionMapper {
    @Select("SELECT * FROM transactions WHERE id = #{id}")
    Transaction findById(@Param("id") Integer id);

    @Select("SELECT * FROM transactions WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Transaction> findByUserId(@Param("userId") Integer userId);

    int insert(Transaction transaction);
}
