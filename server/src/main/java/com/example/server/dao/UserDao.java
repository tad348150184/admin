package com.example.server.dao;


import com.example.server.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  UserDao extends JpaRepository<User, Long> {
    //jpa api复杂，无法用map接收，因而业务上更多选用mybatis
    @Query(value = "select * from user inner join role on user.role_id=role.id",nativeQuery = true)
    List<User> findAll();

    User findByUserNameAndPassword(String userName, String passWord);

}
