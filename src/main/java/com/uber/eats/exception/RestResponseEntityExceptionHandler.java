package com.uber.eats.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import com.uber.eats.response.ErrorResponseDTO;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	MessageSource messageSource;

	@Value("${app.general.error}")
	String defaultMessage;

	@ExceptionHandler(value = { DataInvalidException.class })
	protected ResponseEntity<Object> handleInvaildData(DataInvalidException ex, WebRequest request) {

		log.info("DataInvalidException : " + ex.toString());
		ex.printStackTrace();

		ErrorResponseDTO response = new ErrorResponseDTO(ex.getMessage() != null ? ex.getMessage() : defaultMessage);
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { ForbiddenException.class })
	protected ResponseEntity<Object> handleInvaildData(ForbiddenException ex, WebRequest request) {

		log.info("ForbiddenException : " + ex.toString());
		ex.printStackTrace();

		ErrorResponseDTO response = new ErrorResponseDTO(ex.getMessage() != null ? ex.getMessage()
				: messageSource.getMessage("app.unauthorized.message", null, null));
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler(value = { AccessDeniedException.class })
	protected ResponseEntity<Object> handleInvaildData(AccessDeniedException ex, WebRequest request) {

		log.info("Exception : " + ex.toString());
		ex.printStackTrace();

		ErrorResponseDTO response = new ErrorResponseDTO(
				messageSource.getMessage("app.unauthorized.message", null, null));
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<Object> handleInvaildData(ConstraintViolationException ex, WebRequest request) {

		log.info("Exception : " + ex.toString());
		ex.printStackTrace();

		List<String> messageList = new ArrayList<>();

		for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
			messageList.add(constraintViolation.getMessage());
		}

		ErrorResponseDTO response = new ErrorResponseDTO(messageList.stream().collect(Collectors.joining("\n")));
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<Object> handleInvaildData(Exception ex, WebRequest request) {

		log.info("Exception : " + ex.toString());
		ex.printStackTrace();

		ErrorResponseDTO response = new ErrorResponseDTO(messageSource.getMessage("app.general.error", null, null));
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(value = { RuntimeException.class })
	protected ResponseEntity<Object> handleInvaildData(RuntimeException ex, WebRequest request) {

		log.info("Exception : " + ex.toString());
		ex.printStackTrace();

		ErrorResponseDTO response = new ErrorResponseDTO(messageSource.getMessage("app.general.error", null, null));
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.info("MethodArgumentNotValidException : " + ex.toString());
		ex.printStackTrace();

		String message = "";
		FieldError error = ex.getBindingResult().getFieldError();
		if (error != null) {
			message = error.getDefaultMessage();
		} else {
			ObjectError objError = ex.getBindingResult().getGlobalError();
			if (objError != null) {
				message = objError.getDefaultMessage();
			}
		}
		ErrorResponseDTO response = new ErrorResponseDTO(message);
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (!(body instanceof ErrorResponseDTO)) {
			log.info("System Exception : " + ex.toString());
			ex.printStackTrace();
			body = new ErrorResponseDTO(ex.getMessage());
		}

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		return new ResponseEntity<>(body, headers, status);
	}
}
