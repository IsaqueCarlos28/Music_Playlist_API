package senac.tsi.Music_Playlist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import senac.tsi.Music_Playlist.dtos.ErrorResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        ErrorResponse body = baseResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                ex.getMessage()
        ).build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleEndpointNotFound(
            NoHandlerFoundException ex
    ) {

        ErrorResponse body = baseResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                "Endpoint not found"
        ).build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            NotFoundException ex
    ) {

        ErrorResponse body = baseResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                ex.getMessage()
        )
                .resource(ex.getResource())
                .field(ex.getField())
                .value(ex.getValue())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {

        ErrorResponse body = baseResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage()
        ).build();

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(
            Exception ex
    ) {

        ErrorResponse body = baseResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Method Not Allowed",
                ex.getMessage()
        ).build();

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ErrorResponse body = baseResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                "One or more fields are invalid"
        )
                .errors(errors)
                .build();

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerValidation(
            HandlerMethodValidationException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getAllErrors().forEach(error -> {

            String field = "field";

            if (error instanceof FieldError fieldError) {
                field = fieldError.getField();
            }

            errors.put(field, error.getDefaultMessage());
        });

        ErrorResponse body = baseResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                "One or more fields are invalid"
        )
                .errors(errors)
                .build();

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex
    ) {

        ErrorResponse body = baseResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Business rule violation",
                ex.getMessage()
        )
                .resource(ex.getResource())
                .field(ex.getField())
                .value(ex.getValue())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(body);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(
            ConflictException ex
    ) {

        ErrorResponse body = baseResponse(
                HttpStatus.CONFLICT,
                "Conflict rule violation",
                ex.getMessage()
        )
                .resource(ex.getResource())
                .field(ex.getField())
                .value(ex.getValue())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex
    ) {

        ErrorResponse body = baseResponse(
                HttpStatus.FORBIDDEN,
                "Forbidden",
                "You do not have permission to access this resource"
        ).build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex
    ) {

        ErrorResponse body = baseResponse(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                ex.getMessage()
        ).build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(
            HttpMediaTypeNotSupportedException ex
    ) {

        ErrorResponse body = baseResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Unsupported Media Type",
                ex.getMessage()
        ).build();

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(body);
    }

    private ErrorResponse.ErrorResponseBuilder baseResponse(
            HttpStatus status,
            String error,
            String message
    ) {

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message);
    }
}
