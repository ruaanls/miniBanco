package br.com.fiap.minibanco.core.transactionals.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataUsersTransactionDTO
{
    private String nomeCompleto;
    private String cpf;
    private String tipo;

}
