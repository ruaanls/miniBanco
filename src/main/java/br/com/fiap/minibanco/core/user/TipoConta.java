package br.com.fiap.minibanco.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter

public enum TipoConta
{
    COMUM("Comum"),
    LOJISTAS("Lojistas");

    private String tipo;

}
