package br.com.fiap.minibanco.core.transactionals;

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
public class Transaction
{
    private Long id;
    private BigDecimal valor;
    private User usuarioEnvio;
    private User usuarioRecebimento;
    private Instant dataHora;
}
