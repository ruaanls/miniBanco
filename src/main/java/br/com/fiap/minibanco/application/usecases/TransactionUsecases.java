package br.com.fiap.minibanco.application.usecases;

import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;

import java.math.BigDecimal;

public interface TransactionUsecases
{
    TransactionResponseDTO transferir(TransactionRequestDTO transactionRequestDTO);
    //boolean verificarTransacao(TransactionRequestDTO transactionRequestDTO);
}
