package br.com.fiap.minibanco.domain.ports.outbound;

import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.TransactionJPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepositoryPort
{
    TransactionJPA transferir(TransactionJPA transactionJPA);
    Page<TransactionJPA> findAllTransactionsJpaByCpf(String cpf, Pageable pageable);

}
