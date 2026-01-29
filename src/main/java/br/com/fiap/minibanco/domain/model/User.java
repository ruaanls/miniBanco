package br.com.fiap.minibanco.domain.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;
    private String senha;
    private BigDecimal saldo;
}
