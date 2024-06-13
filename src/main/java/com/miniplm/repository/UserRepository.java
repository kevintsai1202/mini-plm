package com.miniplm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.Form;
import com.miniplm.entity.ZAccount;

public interface UserRepository extends JpaRepository<ZAccount, String> {
	Optional<ZAccount> findByEmail(String email);
	
	Optional<ZAccount> findByUsername(String username);
}
