package com.uber.eats.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uber.eats.entity.CountryEntity;
import com.uber.eats.enums.StatusType;
import com.uber.eats.response.BasicIdAndNameResponseDTO;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
	
	@Query(value = " SELECT new com.uber.eats.response.BasicIdAndNameResponseDTO("
							    + " country.id,"
							    + " country.name"
							    + " )"
							    + " FROM CountryEntity country"
							    + " WHERE country.status = ?1"
							    + " ORDER BY country.name ASC")
	List<BasicIdAndNameResponseDTO> findAllByStatus(StatusType status);

	@Query(value = " SELECT country FROM CountryEntity country"
							    + " WHERE country.id IN (?1)"
							    + " AND country.status = ?2")
	Set<CountryEntity> findAllByIdInAndStatus(Set<Long> countryIdList, StatusType status);

}
