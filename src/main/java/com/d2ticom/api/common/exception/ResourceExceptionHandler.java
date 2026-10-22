package com.d2ticom.api.common.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectnotFoundException.class)
	public ResponseEntity<StandardError> objectNotFoundException(ObjectnotFoundException ex,
			HttpServletRequest request) {

		StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
				"Object Not Found", ex.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	// 🟢 Captura violações de chave única do banco e de transações
	@ExceptionHandler({DataIntegrityViolationException.class, org.hibernate.exception.ConstraintViolationException.class})
	public ResponseEntity<StandardError> dataIntegrityViolationException(Exception ex, HttpServletRequest request) {

		String mensagem = "Violação de integridade nos dados.";
		
		// Concatena a mensagem principal com a causa para garantir que a constraint seja encontrada
		String mensagemCompleta = ex.getMessage() != null ? ex.getMessage() : "";
		if (ex.getCause() != null && ex.getCause().getMessage() != null) {
			mensagemCompleta += " " + ex.getCause().getMessage();
		}

		if (mensagemCompleta.contains("uk_instrumento_nome_empresa")) {
			mensagem = "Já existe um instrumento cadastrado com este nome nesta congregação.";
		} else if (mensagemCompleta.contains("uk_departamento_nome_empresa")) {
			mensagem = "Já existe um departamento cadastrado com este nome nesta congregação.";
		} else if (mensagemCompleta.contains("usuarios_email_key") || mensagemCompleta.contains("email")) {
			mensagem = "Já existe um usuário cadastrado com este e-mail.";
		}

		StandardError error = new StandardError(
				System.currentTimeMillis(), 
				HttpStatus.CONFLICT.value(),
				"Conflito de dados", 
				mensagem, 
				request.getRequestURI()
		);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	// 🟢 Captura regras de negócio lançadas com throw new RuntimeException("...")
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<StandardError> runtimeException(RuntimeException ex, HttpServletRequest request) {
		StandardError error = new StandardError(
				System.currentTimeMillis(),
				HttpStatus.BAD_REQUEST.value(),
				"Regra de Negócio",
				ex.getMessage(),
				request.getRequestURI()
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validationErrors(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		ValidationError errors = new ValidationError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				"Validation error", "Erro na validação dos campos", request.getRequestURI());

		for (FieldError x : ex.getBindingResult().getFieldErrors()) {
			errors.addError(x.getField(), x.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<StandardError> accessDeniedException(
	        AccessDeniedException ex,
	        HttpServletRequest request) {

	    StandardError error = new StandardError(
	            System.currentTimeMillis(),
	            HttpStatus.FORBIDDEN.value(),
	            "Forbidden",
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
}
