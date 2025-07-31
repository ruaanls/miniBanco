package br.com.fiap.minibanco.application.usecases;

import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;

public interface VerificationTransactionUsecases
{
    boolean verificarTransacao(TransactionRequestDTO request);
    void validarAutorizacaoTransacao(TransactionRequestDTO requestDTO);
}
