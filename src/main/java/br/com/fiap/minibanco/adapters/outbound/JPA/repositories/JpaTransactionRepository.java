package br.com.fiap.minibanco.adapters.outbound.JPA.repositories;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTransactionRepository extends JpaRepository<TransactionJPA, Long>
{
    Optional<TransactionJPA> findTransactionJPAById(Long id);
}
