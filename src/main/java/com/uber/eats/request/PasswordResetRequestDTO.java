package com.uber.eats.request;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordResetRequestDTO {
	
	@NotBlank
	String token;
	
	@NotBlank
	String password;

}
