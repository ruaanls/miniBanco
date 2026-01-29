package br.com.fiap.minibanco.adapters.inbound.controller;

import br.com.fiap.minibanco.application.usecases.TransactionUsecases;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banco")
@Tag(name = "Transações", description = "Endpoints para operações bancárias: transferências PIX e consulta de extrato. Requer autenticação JWT.")
@SecurityRequirement(name = "Bearer Authentication")
public class TransactionController
{

    @Autowired
    private final TransactionUsecases transactionService;

    public TransactionController(TransactionUsecases transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Realizar transferência PIX",
            description = """
                    Executa uma transferência PIX entre dois usuários. 
                    
                    **Regras de negócio:**
                    - O remetente deve ter saldo suficiente
                    - Não é possível transferir para o mesmo CPF
                    - Lojistas só podem receber, não podem enviar valores
                    - O valor deve ser maior que zero
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transferência realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação ou tentativa de transferência para o mesmo CPF"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Saldo insuficiente, transação negada por segurança ou lojista tentando enviar"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CPF do remetente ou destinatário não encontrado"
            )
    })
    @PostMapping("/pix")
    public ResponseEntity<TransactionResponseDTO> transacao (@RequestBody @Valid TransactionRequestDTO request)
    {
        TransactionResponseDTO response = transactionService.transferir(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Consultar extrato de transações",
            description = """
                    Retorna o extrato paginado de todas as transações (envio e recebimento) de um usuário.
                    
                    **Paginação:**
                    - Cada página contém 2 transações
                    - Use o parâmetro `page` para navegar (começa em 0)
                    - As transações são ordenadas por ID em ordem crescente
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Extrato consultado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token JWT inválido ou ausente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CPF não encontrado ou nenhuma transação encontrada para o usuário"
            )
    })
    @GetMapping("/extrato/{cpf}")
    public ResponseEntity<Page<TransactionResponseDTO>> extrato (
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(defaultValue = "0") Integer page, 
            @Parameter(description = "CPF do usuário no formato XXX.XXX.XXX-XX", example = "123.456.789-00")
            @PathVariable String cpf)
    {
        Pageable pageable = PageRequest
                .of(page, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<TransactionResponseDTO> response =  transactionService.extrato(pageable, cpf);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
