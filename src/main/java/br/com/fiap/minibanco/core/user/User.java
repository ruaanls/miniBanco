package br.com.fiap.minibanco.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User
{
    private String name;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private TipoConta tipoConta;
    private String senha;
    private BigDecimal saldo;
}
