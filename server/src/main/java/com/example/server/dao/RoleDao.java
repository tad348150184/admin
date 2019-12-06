package com.example.server.dao;


import com.example.server.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, Long> {
    @Query(value = "select * from role",nativeQuery = true)
    List<Role> findAll();

    List<Role> findById(long id);

}
