package com.oscar.conduit.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // validation errors for request dto
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(
      MethodArgumentNotValidException ex) {
    String message = ex
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse(message));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleRequestNotReadable(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse("Required request body is missing"));
  }

  @ExceptionHandler(InvalidEmailOrPasswordException.class)
  public ResponseEntity<ErrorResponse> handleInvalidEmailOrPassword(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(UserWithEmailExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserWithEmailExists(Exception ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
        new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(Exception ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
        new ErrorResponse("You do not have permission to access this resource"));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(NoResourceFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("The route " + ex.getResourcePath() + " does not exist"));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleRequestNotReadable(HttpRequestMethodNotSupportedException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse("Request method " + ex.getMethod() + " is not supported"));
  }

  // handles all remaining errors
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ErrorResponse("something went wrong"));
  }
}
