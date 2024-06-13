package com.miniplm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String roleName);
}
