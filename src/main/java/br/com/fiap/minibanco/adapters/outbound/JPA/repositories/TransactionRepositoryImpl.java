package br.com.fiap.minibanco.adapters.outbound.JPA.repositories;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;
import br.com.fiap.minibanco.core.transactionals.ports.TransactionRepositoryPort;
import br.com.fiap.minibanco.infra.exception.HavenotTransationsExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionRepositoryImpl implements TransactionRepositoryPort
{
    private final JpaTransactionRepository repository;

    @Override
    public TransactionJPA transferir(TransactionJPA transactionJPA) {
        return this.repository.save(transactionJPA);
    }

    @Override
    public Page<TransactionJPA> findAllTransactionsJpaByCpf(String cpf, Pageable pageable) {
        Page<TransactionJPA> allTransactions = this.repository.findAllByUsuarioEnvio_Cpf(cpf, pageable);
        if(allTransactions.getTotalElements() == 0L)
        {
            throw new HavenotTransationsExceptions();
        }
        else
        {
            return allTransactions;
        }
    }




}
