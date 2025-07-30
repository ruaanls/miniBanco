package br.com.fiap.minibanco.core.transactionals.DTO;

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
public class TransactionRequestDTO
{
    private BigDecimal valor;
    private String cpfEnvio;
    private String cpfRecebimento;

}
