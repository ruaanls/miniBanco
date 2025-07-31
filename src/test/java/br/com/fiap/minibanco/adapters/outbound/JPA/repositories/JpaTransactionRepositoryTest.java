package br.com.fiap.minibanco.adapters.outbound.JPA.repositories;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;
import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import br.com.fiap.minibanco.core.user.TipoConta;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class JpaTransactionRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    JpaTransactionRepository transactionRepository;

    @Test
    @DisplayName("Devolver transação através do ID com sucesso")
    void findTransactionJPAById()
    {

        UserJpa envio = new UserJpa(null,"Ruan Lima","123.456.789-00", TipoConta.COMUM,"teste@gmail.com","senha123", new BigDecimal(100));
        UserJpa recebimento = new UserJpa(null,"Pedro Martins","123.456.789-01", TipoConta.LOJISTAS,"teste2@gmail.com","senha789", new BigDecimal(500));
        TransactionJPA transactionJPA = new TransactionJPA(null,new BigDecimal(50), envio, recebimento);
        this.entityManager.persist(envio);
        this.entityManager.persist(recebimento);
        this.entityManager.persist(transactionJPA);
        Long generatedId = transactionJPA.getId();

        Optional<TransactionJPA> resultado = this.transactionRepository.findTransactionJPAById(generatedId);
        assertTrue(resultado.isPresent());
        assertEquals(generatedId, resultado.get().getId());
    }

    @Test
    @DisplayName("Não encontrar o transação passando id inexistente")
    void findTransactionJPAByIdError()
    {
        Long id = 1L;
        UserJpa envio = new UserJpa(null,"Ruan Lima","123.456.789-00", TipoConta.COMUM,"teste@gmail.com","senha123", new BigDecimal(100));
        UserJpa recebimento = new UserJpa(null,"Pedro Martins","123.456.789-01", TipoConta.LOJISTAS,"teste2@gmail.com","senha789", new BigDecimal(500));
        TransactionJPA transactionJPA = new TransactionJPA(null,new BigDecimal(70), envio, recebimento);

        Long generatedId = transactionJPA.getId();

        Optional<TransactionJPA> resultado = this.transactionRepository.findTransactionJPAById(generatedId);
        assertTrue(resultado.isEmpty());

    }


}