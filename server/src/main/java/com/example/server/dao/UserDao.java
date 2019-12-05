package com.example.server.dao;


import com.example.server.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  UserDao extends JpaRepository<User, Long> {
    @Query(value = "select * from user",nativeQuery = true)
    List<User> findAll();

    User findByUserNameAndPassword(String userName, String passWord);

}
