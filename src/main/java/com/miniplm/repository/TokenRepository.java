package com.miniplm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.miniplm.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
	@Query(nativeQuery = true,  value = "select * from MP_TOKEN t inner join Z_ACCOUNT u on t.user_id = u.id where u.id = :id and (t.expired = false and t.revoked = false)")
	List<Token> findAllValidTokenByUser(String id);
	
	@Modifying
	@Query(nativeQuery = true,  value = "update MP_TOKEN set revoked = 1, expired = 1 where USER_ID = :id")
	void disableTokenByUser(String id);

	Optional<Token> findByToken(String token);
}
