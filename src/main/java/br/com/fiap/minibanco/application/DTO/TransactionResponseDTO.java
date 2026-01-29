package br.com.fiap.minibanco.application.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionResponseDTO
{
    private Long id;
    private BigDecimal valor;
    private LocalDateTime dataHora;
    private DataUsersTransactionDTO usuarioEnvio;
    private DataUsersTransactionDTO usuarioRecebimento;
}
