package br.com.fiap.minibanco.core.transactionals.DTO;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.core.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionResponseDTO
{
    private Long id;
    private BigDecimal valor;
    private Instant dataHora;
    private DataUsersTransactionDTO usuarioEnvio;
    private DataUsersTransactionDTO usuarioRecebimento;
}
