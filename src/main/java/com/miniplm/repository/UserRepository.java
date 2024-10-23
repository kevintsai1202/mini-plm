package com.miniplm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.ZAccount;

public interface UserRepository extends JpaRepository<ZAccount, String> {
//	Optional<ZAccount> findByEmail(String email);
	
	Optional<ZAccount> findByUsernameIgnoreCase(String username);
	
	Page<ZAccount> findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(String username, String email, Pageable pageable);	
	Page<ZAccount> findByUsernameContainingIgnoreCase(String username, Pageable pageable);	
	Page<ZAccount> findByEmailContainingIgnoreCase(String username, Pageable pageable);
	
	List<ZAccount> findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(String username, String email);	
	List<ZAccount> findByUsernameContainingIgnoreCase(String username);	
	List<ZAccount> findByEmailContainingIgnoreCase(String username);
}
