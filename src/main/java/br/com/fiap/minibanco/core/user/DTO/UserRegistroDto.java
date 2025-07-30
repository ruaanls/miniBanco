package br.com.fiap.minibanco.core.user.DTO;

import br.com.fiap.minibanco.core.user.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistroDto
{
    private String nome;
    private String cpf;
    private TipoConta tipoConta;
    private String email;
    private String senha;

    private BigDecimal saldo = BigDecimal.ZERO;

}
