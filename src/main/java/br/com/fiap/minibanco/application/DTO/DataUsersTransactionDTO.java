package br.com.fiap.minibanco.application.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
