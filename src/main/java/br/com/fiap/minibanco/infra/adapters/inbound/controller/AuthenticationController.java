package br.com.fiap.minibanco.infra.adapters.inbound.controller;

import br.com.fiap.minibanco.application.usecases.UserUsecases;
import br.com.fiap.minibanco.application.DTO.UserLoginDTO;
import br.com.fiap.minibanco.application.DTO.UserLoginResponseDTO;
import br.com.fiap.minibanco.application.DTO.UserRegistroDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários. Não requerem autenticação JWT.")
public class AuthenticationController
{
    @Autowired
    private UserUsecases userService;

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário existente e retorna um token JWT para acesso aos demais endpoints da API."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserLoginResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação nos dados fornecidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CPF não encontrado no sistema"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginDTO data)
    {
        UserLoginResponseDTO loginResponse =  this.userService.login(data);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);

    }

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário no sistema. O CPF e email devem ser únicos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário registrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação, CPF ou email já cadastrado"
            )
    })
    @PostMapping("/registro")
    public ResponseEntity register(@RequestBody @Valid UserRegistroDto data)
    {
        this.userService.registrar(data);
        return ResponseEntity.ok().build();
    }
}
