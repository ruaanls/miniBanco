package br.com.fiap.minibanco.application.usecases;

import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;

public interface VerificationTransactionUsecases
{
    boolean verificarTransacao(TransactionRequestDTO request);
    void validarAutorizacaoTransacao(TransactionRequestDTO requestDTO);

}
