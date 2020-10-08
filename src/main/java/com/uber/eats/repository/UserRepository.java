package com.uber.eats.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uber.eats.entity.UserEntity;
import com.uber.eats.enums.StatusType;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByUserName(String username);

	@Query(value = " SELECT CASE WHEN COUNT(user) > 0"
								+ " THEN true ELSE false END"
								+ " FROM UserEntity user"
								+ " WHERE LOWER(user.email) = LOWER(?1)"
								+ " AND user.status = ?2")
	Boolean existsByEmailAndStatus(String email, StatusType status);

}
