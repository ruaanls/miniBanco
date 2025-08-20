package br.com.fiap.minibanco.application.usecases;

import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface TransactionUsecases
{
    TransactionResponseDTO transferir(TransactionRequestDTO transactionRequestDTO);
    Page<TransactionResponseDTO> extrato(Pageable pageable, String cpf);
}
