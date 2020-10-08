package com.uber.eats.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uber.eats.entity.CityEntity;
import com.uber.eats.enums.StatusType;
import com.uber.eats.response.BasicIdAndNameResponseDTO;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
	
	@Query(value = " SELECT new com.uber.eats.response.BasicIdAndNameResponseDTO("
							    + " city.id,"
							    + " city.name"
							    + " )"
							    + " FROM CityEntity city"
							    + " WHERE city.state.id = ?1"
							    + " AND city.status = ?2"
							    + " ORDER BY city.name ASC")
	List<BasicIdAndNameResponseDTO> findAllByStateIdAndStatus(Long stateId, StatusType status);

	@Query(value = " SELECT city FROM CityEntity city"
								+ " WHERE city.id = ?1"
								+ " AND city.status = ?2")
	Optional<CityEntity> findByIdAndStatus(Long id, StatusType status);
	
}
