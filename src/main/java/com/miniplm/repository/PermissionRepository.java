package com.miniplm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.Permission;
import com.miniplm.entity.Privilege;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Permission findByPermissionName(String permissionName);
}
