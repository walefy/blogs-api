package org.walefy.adivice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.walefy.exception.NotFoundException;
import org.walefy.exception.UnauthorizedActionException;
import org.walefy.exception.UserAlreadyRegistered;

@ControllerAdvice
public class GeneralControllerAdvice {
  @ExceptionHandler({ UserAlreadyRegistered.class })
  public ResponseEntity<Map<String, String>> handleConflict(Exception e) {
    Map<String, String> response = new HashMap<>(1);
    response.put("message", e.getMessage());

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler({ MethodArgumentNotValidException.class })
  public ResponseEntity<Map<String, Object>> handleInvalidArgument(
      MethodArgumentNotValidException e
  ) {
    List<String> fieldErrors= e.getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();

    Map<String, Object> response = new HashMap<>(2);
    response.put("message", "some invalid fields");
    response.put("stack", fieldErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler({ NotFoundException.class })
  public ResponseEntity<Map<String, String>> handleNotFound(Exception e) {
    Map<String, String> response = Map.of("message", e.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler({ UnauthorizedActionException.class })
  public ResponseEntity<Map<String, String>> handleUnauthorizedActions(Exception e) {
    Map<String, String> response = Map.of("message", e.getMessage());

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }
}
