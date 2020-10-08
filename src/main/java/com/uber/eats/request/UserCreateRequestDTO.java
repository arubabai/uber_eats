package com.uber.eats.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequestDTO {

	@NotBlank
	String name;

	@Email
	String email;

	@NotBlank
	String phone;

	@NotNull
	AddressRequestDTO address;

	@NotBlank
	String companyName;;

	@NotBlank
	String companyPhone;

	@NotBlank
	String password;

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class AddressRequestDTO {
		
		String address;
		
		Long cityId;
		
	}
	
}
