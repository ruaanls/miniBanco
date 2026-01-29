package br.com.fiap.minibanco.infra.adapters.outbound.persistence.repositories;

import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.TransactionJPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTransactionRepository extends JpaRepository<TransactionJPA, Long>
{
    Optional<TransactionJPA> findTransactionJPAById(Long id);
    Page<TransactionJPA> findAllByUsuarioEnvio_Cpf(String cpf, Pageable pageable);
}
