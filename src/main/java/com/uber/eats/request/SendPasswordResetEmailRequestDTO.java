package com.uber.eats.request;

import javax.validation.constraints.Email;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendPasswordResetEmailRequestDTO {
	
	@Email
	String email;

}
