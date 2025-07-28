package br.com.fiap.minibanco.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ValidationErrorResponse
{

    private HttpStatus status;
    private String message;
    private List<String> errors;
}
