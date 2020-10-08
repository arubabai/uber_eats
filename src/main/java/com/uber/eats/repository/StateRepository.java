package com.uber.eats.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uber.eats.entity.StateEntity;
import com.uber.eats.enums.StatusType;
import com.uber.eats.response.BasicIdAndNameResponseDTO;

public interface StateRepository extends JpaRepository<StateEntity, Long> {
	
	@Query(value = " SELECT new com.uber.eats.response.BasicIdAndNameResponseDTO("
							    + " state.id,"
							    + " state.name"
							    + " )"
							    + " FROM StateEntity state"
							    + " WHERE state.country.id = ?1"
							    + " AND state.status = ?2"
							    + " ORDER BY state.name ASC")
	List<BasicIdAndNameResponseDTO> findAllByCountryIdAndStatus(Long countryId, StatusType status);

}
