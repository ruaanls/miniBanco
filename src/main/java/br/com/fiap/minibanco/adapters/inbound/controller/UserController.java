package br.com.fiap.minibanco.adapters.inbound.controller;

import br.com.fiap.minibanco.application.usecases.UserUsecases;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import br.com.fiap.minibanco.core.user.DTO.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private UserUsecases userService;

    @GetMapping("/saldo/{cpf}")
    public ResponseEntity<UserResponseDTO> saldo (@PathVariable String cpf)
    {
        UserResponseDTO response = userService.findUserResponseByCpf(cpf);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
