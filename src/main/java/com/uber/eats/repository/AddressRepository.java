package com.uber.eats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uber.eats.entity.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

}
