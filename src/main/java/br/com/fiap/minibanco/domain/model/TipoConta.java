package br.com.fiap.minibanco.domain.model;

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
