package br.com.fiap.minibanco.adapters.outbound.JPA.repositories;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;
import br.com.fiap.minibanco.core.transactionals.ports.TransactionRepositoryPort;
import br.com.fiap.minibanco.infra.exception.TransactionNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class TransactionRepositoryImpl implements TransactionRepositoryPort
{
    @Autowired
    private final JpaTransactionRepository repository;

    @Override
    public TransactionJPA transferir(TransactionJPA transactionJPA) {
        return this.repository.save(transactionJPA);
    }

    @Override
    public Optional<TransactionJPA> findTransactionJPAById(Long id) {
        if(this.repository.findTransactionJPAById(id).isEmpty())
        {
            throw new TransactionNotFoundException(id);
        }
        else
        {
            return this.repository.findTransactionJPAById(id);
        }
    }


}
