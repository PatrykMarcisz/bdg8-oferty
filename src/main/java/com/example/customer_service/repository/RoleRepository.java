package com.example.customer_service.repository;

import com.example.customer_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // SELECT * FROM role WHERE role_name = ?
    Role findFirstByRoleName(String roleName);
}
