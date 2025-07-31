package br.com.fiap.minibanco.application.service;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.adapters.outbound.JPA.repositories.JpaUserRepository;
import br.com.fiap.minibanco.core.transactionals.ports.TransactionRepositoryPort;
import br.com.fiap.minibanco.core.user.TipoConta;
import br.com.fiap.minibanco.core.user.ports.UserRepositoryPort;
import br.com.fiap.minibanco.utils.mapper.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
class TransactionServiceImplTest {

    @Mock
    private TransactionRepositoryPort transactionRepository;

    @Mock
    private  UserSerivceImpl userSerivceImpl;

    @Mock
    private  TransactionMapper transactionMapper;

    @Mock
    private JpaUserRepository userRepositoryPort;

    @Autowired
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Realizar transação com sucesso")
    void transferir()
    {
        // TALVEZ PRECISE DO INSTANT

        UserJpa envio = new UserJpa("1", "Ruan Lima", "123.456.789-00", TipoConta.COMUM,"ruan@gmail.com","senha123", new java.math.BigDecimal(100) );
        UserJpa recebimento = new UserJpa("2", "João Pedro", "123.456.789-10", TipoConta.COMUM,"joao@gmail.com","senha123", new java.math.BigDecimal(1000) );

        when(userSerivceImpl.findUserJpaByCpf("1")).thenReturn(envio);
        when(userSerivceImpl.findUserJpaByCpf("2")).thenReturn(recebimento);
        when()
    }
}