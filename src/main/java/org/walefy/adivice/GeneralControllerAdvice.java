package org.walefy.adivice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.walefy.exception.UserAlreadyRegistred;

@ControllerAdvice
public class GeneralControllerAdvice {
  @ExceptionHandler({ UserAlreadyRegistred.class })
  public ResponseEntity<Map<String, String>> handleConflict(Exception e) {
    Map<String, String> response = new HashMap<>(1);
    response.put("message", e.getMessage());

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler({ MethodArgumentNotValidException.class })
  public ResponseEntity<Map<String, String>> handleInvalidArgument(
      MethodArgumentNotValidException e
  ) {
    FieldError fieldError = e.getBindingResult().getFieldError();

    Map<String, String> response = new HashMap<>(1);
    response.put("message", fieldError != null ? fieldError.getDefaultMessage() : null);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
