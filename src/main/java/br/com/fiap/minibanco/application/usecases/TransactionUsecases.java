package br.com.fiap.minibanco.application.usecases;

import br.com.fiap.minibanco.application.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.application.DTO.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionUsecases
{
    TransactionResponseDTO transferir(TransactionRequestDTO transactionRequestDTO);
    Page<TransactionResponseDTO> extrato(Pageable pageable, String cpf);
}
