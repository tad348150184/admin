package com.example.client.dao;


import com.example.client.pojo.AdminLog;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AdminLogDao extends JpaRepository<AdminLog, Long> {
    List<AdminLog> findAll();

    AdminLog save(AdminLog adminLog);

}
