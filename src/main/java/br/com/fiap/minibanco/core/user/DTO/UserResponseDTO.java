package br.com.fiap.minibanco.core.user.DTO;

import br.com.fiap.minibanco.core.user.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO
{
    private String nome;
    private String cpf;
    private TipoConta tipoConta;
    private BigDecimal saldo;
}
