package br.com.fiap.minibanco.infra.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(UserExistsException.class)
    private ResponseEntity<AuthInvalid> registerFailedHandler(UserExistsException exception)
    {
        AuthInvalid Exception = new AuthInvalid(HttpStatus.BAD_REQUEST,exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Exception);
    }

    @ExceptionHandler(OracleInputException.class)
    private ResponseEntity<AuthInvalid> oracleInputHandler(OracleInputException exception)
    {
        AuthInvalid Exception = new AuthInvalid(HttpStatus.INTERNAL_SERVER_ERROR,exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Exception);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    private ResponseEntity<AuthInvalid> transactionNotFoundHandler(TransactionNotAllowedException exception)
    {
        AuthInvalid Exception = new AuthInvalid(HttpStatus.NOT_FOUND,exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Exception);
    }

    @ExceptionHandler(SameCPFException.class)
    private ResponseEntity<AuthInvalid> sameCpfHandler(SameCPFException exception)
    {
        AuthInvalid Exception = new AuthInvalid(HttpStatus.BAD_REQUEST,exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Exception);
    }

    @ExceptionHandler(TransactionNotAllowedException.class)
    private ResponseEntity<AuthInvalid> transactionNotAllowedHandler(TransactionNotAllowedException exception)
    {
        AuthInvalid Exception = new AuthInvalid(HttpStatus.FORBIDDEN,exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Exception);
    }

    @ExceptionHandler(BalanceInsufficientException.class)
    private ResponseEntity<AuthInvalid> balanceInsufficientHandler(BalanceInsufficientException exception)
    {
        AuthInvalid Exception = new AuthInvalid(HttpStatus.FORBIDDEN,exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Exception);
    }

    @ExceptionHandler(EmailExistsException.class)
    private ResponseEntity<AuthInvalid> emailExistsHandler(EmailExistsException exception)
    {
        AuthInvalid Exception = new AuthInvalid(HttpStatus.BAD_REQUEST,exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Exception);
    }


    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<AuthInvalid> userNotFoundHandler(UserNotFoundException exception)
    {
        AuthInvalid Exception = new AuthInvalid(HttpStatus.NOT_FOUND,exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Exception);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String message = "Dados inválidos fornecidos";

        // Verifica se é erro de ENUM
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();

            if (invalidFormatException.getTargetType().isEnum()) {
                Object[] enumValues = invalidFormatException.getTargetType().getEnumConstants();
                String validValues = java.util.Arrays.toString(enumValues)
                        .replace("[", "")
                        .replace("]", "");

                message = String.format(
                        "Valor inválido '%s' para o campo '%s'. Valores aceitos: %s",
                        invalidFormatException.getValue(),
                        invalidFormatException.getPath().get(invalidFormatException.getPath().size() - 1).getFieldName(),
                        validValues
                );
            }
        }

        AuthInvalid authInvalid = new AuthInvalid(HttpStatus.BAD_REQUEST, message);
        return new ResponseEntity<>(authInvalid, HttpStatus.BAD_REQUEST);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}