package com.uber.eats.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uber.eats.entity.PasswordResetTokenEntity;
import com.uber.eats.entity.UserEntity;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
	
	@Query(value = " SELECT passwordResetToken"
								+ " FROM PasswordResetTokenEntity passwordResetToken"
								+ " WHERE passwordResetToken.user = ?1")
	Optional<PasswordResetTokenEntity> findByUser(UserEntity user);

	@Query(value = " SELECT passwordResetToken"
								+ " FROM PasswordResetTokenEntity passwordResetToken"
								+ " WHERE passwordResetToken.token = ?1")
	Optional<PasswordResetTokenEntity> findByToken(String token);
	
}
