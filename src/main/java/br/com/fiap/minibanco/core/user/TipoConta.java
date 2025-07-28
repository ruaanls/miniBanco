package br.com.fiap.minibanco.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoConta
{
    COMUM("Comum"),
    LOJISTAS("Lojistas");

    private String tipo;

}
