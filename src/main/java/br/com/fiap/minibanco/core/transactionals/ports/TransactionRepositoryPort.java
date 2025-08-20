package br.com.fiap.minibanco.core.transactionals.ports;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryPort
{
    TransactionJPA transferir(TransactionJPA transactionJPA);
    Page<TransactionJPA> findAllTransactionsJpaByCpf(String cpf, Pageable pageable);

}
