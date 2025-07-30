package br.com.fiap.minibanco.core.transactionals.ports;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;

import java.util.Optional;

public interface TransactionRepositoryPort
{
    TransactionJPA transferir(TransactionJPA transactionJPA);
    Optional<TransactionJPA> findTransactionJPAById(Long id);
}
