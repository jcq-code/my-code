package com.example.smartshop.mapper;

import com.example.smartshop.entity.User;
import com.example.smartshop.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM t_user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Select("SELECT r.* FROM t_role r INNER JOIN t_user_role ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    List<Role> findRolesByUserId(@Param("userId") Integer userId);
}
