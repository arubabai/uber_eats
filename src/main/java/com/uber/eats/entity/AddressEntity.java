package com.uber.eats.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_address")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;

	@Column(name = "address", columnDefinition = "Text")
	@NotBlank
	String address;

	@ManyToOne
	@JoinColumn(name = "city_id")
	CityEntity city;

	@ManyToOne
	@JoinColumn(name = "state_id")
	StateEntity state;

	@ManyToOne
	@JoinColumn(name = "country_id")
	CountryEntity country;

	@Column(name = "zip_code")
	@NotBlank
	String zipCode;

}
