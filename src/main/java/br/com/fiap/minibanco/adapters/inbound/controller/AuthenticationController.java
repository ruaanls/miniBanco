package br.com.fiap.minibanco.adapters.inbound.controller;

import br.com.fiap.minibanco.application.usecases.UserUsecases;
import br.com.fiap.minibanco.core.user.DTO.UserLoginDTO;
import br.com.fiap.minibanco.core.user.DTO.UserLoginResponseDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController
{
    @Autowired
    private UserUsecases userService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginDTO data)
    {
        UserLoginResponseDTO loginResponse =  this.userService.login(data);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);

    }

    @PostMapping("/registro")
    public ResponseEntity register(@RequestBody @Valid UserRegistroDto data)
    {
        this.userService.registrar(data);
        return ResponseEntity.ok().build();
    }
}
