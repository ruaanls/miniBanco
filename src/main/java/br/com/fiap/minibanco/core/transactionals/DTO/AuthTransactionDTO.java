package br.com.fiap.minibanco.core.transactionals.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthTransactionDTO
{
    private String status;
    private DataResponse data;
}
