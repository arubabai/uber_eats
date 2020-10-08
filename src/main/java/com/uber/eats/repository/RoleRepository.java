package com.uber.eats.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uber.eats.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	
	@Query(value = " SELECT role FROM RoleEntity role"
								+ " WHERE role.shortCode = ?1")
	Optional<RoleEntity> findByShortCode(String shortCode);

}
