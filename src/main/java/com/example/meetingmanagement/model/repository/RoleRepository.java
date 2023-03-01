package com.example.meetingmanagement.model.repository;

import com.example.meetingmanagement.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleEnum(Role.RoleEnum roleEnum);
}
