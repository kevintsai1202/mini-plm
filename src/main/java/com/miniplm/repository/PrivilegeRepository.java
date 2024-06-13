package com.miniplm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.Privilege;
import com.miniplm.entity.Role;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
	Privilege findByPrivilegeName(String privilegeName);
}
