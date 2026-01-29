package br.com.fiap.minibanco.adapters.inbound.controller;

import br.com.fiap.minibanco.application.usecases.UserUsecases;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import br.com.fiap.minibanco.core.user.DTO.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários. Requer autenticação JWT.")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController
{
    @Autowired
    private UserUsecases userService;

    @Operation(
            summary = "Consultar saldo",
            description = "Retorna as informações do usuário incluindo nome, CPF, tipo de conta e saldo atual."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Saldo consultado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token JWT inválido ou ausente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CPF não encontrado no sistema"
            )
    })
    @GetMapping("/saldo/{cpf}")
    public ResponseEntity<UserResponseDTO> saldo (@PathVariable String cpf)
    {
        UserResponseDTO response = userService.findUserResponseByCpf(cpf);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário do sistema. Apenas o próprio usuário pode excluir sua conta."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado - não é possível excluir contas de outros usuários"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CPF não encontrado no sistema"
            )
    })
    @DeleteMapping("/{cpf}")
    public ResponseEntity delete (@PathVariable String cpf)
    {
        this.userService.deleteUser(cpf);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Atualizar dados do usuário",
            description = "Atualiza as informações de um usuário existente. O email deve ser único se alterado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação ou email já cadastrado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado - não é possível atualizar contas de outros usuários"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CPF não encontrado no sistema"
            )
    })
    @PutMapping("/{cpf}")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserRegistroDto userRegistroDto, @PathVariable String cpf )
    {
        UserResponseDTO responseDTO = this.userService.updateUser(userRegistroDto,cpf);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
