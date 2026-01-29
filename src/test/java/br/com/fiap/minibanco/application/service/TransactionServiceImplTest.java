package br.com.fiap.minibanco.application.service;

import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.TransactionJPA;
import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.UserJpa;
import br.com.fiap.minibanco.infra.adapters.outbound.persistence.repositories.TransactionRepositoryImpl;
import br.com.fiap.minibanco.infra.adapters.outbound.persistence.repositories.UserRepositoryImpl;
import br.com.fiap.minibanco.application.DTO.DataUsersTransactionDTO;
import br.com.fiap.minibanco.application.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.application.DTO.TransactionResponseDTO;
import br.com.fiap.minibanco.domain.model.TipoConta;
import br.com.fiap.minibanco.utils.mapper.TransactionMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



class TransactionServiceImplTest {

    @Mock
    private TransactionRepositoryImpl transactionRepository;

    @Mock
    private  UserSerivceImpl userSerivceImpl;

    @Mock
    private UserRepositoryImpl userRepository;


    @Mock
    private VerificationTransactionsImpl verification;

    @Mock
    private TransactionMapper transactionMapper;

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


        UserJpa envio = new UserJpa("1", "Ruan Lima", "123.456.789-00", TipoConta.COMUM,"ruan@gmail.com","senha123", new java.math.BigDecimal(100) );
        UserJpa recebimento = new UserJpa("2", "João Pedro", "123.456.789-10", TipoConta.COMUM,"joao@gmail.com","senha123", new java.math.BigDecimal(1000) );

        TransactionJPA transaction = new TransactionJPA(null,new BigDecimal(1000), envio, recebimento);
        DataUsersTransactionDTO envioData = new DataUsersTransactionDTO(envio.getNome_completo(), envio.getCpf(), envio.getTipo().toString());
        DataUsersTransactionDTO recebimentoData = new DataUsersTransactionDTO(recebimento.getNome_completo(), recebimento.getCpf(), recebimento.getTipo().toString());
        TransactionResponseDTO response = new TransactionResponseDTO(transaction.getId(), transaction.getValor(), Instant.now(), envioData, recebimentoData);


        when(userSerivceImpl.findUserJpaByCpf("1")).thenReturn(envio);
        when(userSerivceImpl.findUserJpaByCpf("2")).thenReturn(recebimento);
        when(verification.verificarTransacao(any())).thenReturn(true);

        when(transactionRepository.transferir(any())).thenReturn(transaction);
        doNothing().when(userRepository).registrar(any(UserJpa.class));
        when(transactionMapper.requestToTransaction(any(),any(),any(),any())).thenReturn(transaction);
        when(transactionMapper.userDtoToDataUsersTransactionDto(envio)).thenReturn(envioData);
        when(transactionMapper.userDtoToDataUsersTransactionDto(recebimento)).thenReturn(recebimentoData);
        when(transactionMapper.transactionToResponse(any(),any(),any())).thenReturn(response);


        TransactionRequestDTO request = new TransactionRequestDTO(new BigDecimal(1000),"1", "2");
        transactionService.transferir(request);

        verify(transactionRepository, times(1)).transferir(any());

        envio.setSaldo(new BigDecimal(0));

        recebimento.setSaldo(new BigDecimal(1100));
        verify(userRepository, times(2)).registrar(any(UserJpa.class));

    }

    @Test
    @DisplayName("Erro de transação por verificacao")
    void transferirError() throws Exception
    {


        UserJpa envio = new UserJpa("1", "Ruan Lima", "123.456.789-00", TipoConta.COMUM,"ruan@gmail.com","senha123", new java.math.BigDecimal(100) );
        UserJpa recebimento = new UserJpa("2", "João Pedro", "123.456.789-10", TipoConta.COMUM,"joao@gmail.com","senha123", new java.math.BigDecimal(1000) );

        TransactionJPA transaction = new TransactionJPA(null,new BigDecimal(1000), envio, recebimento);
        DataUsersTransactionDTO envioData = new DataUsersTransactionDTO(envio.getNome_completo(), envio.getCpf(), envio.getTipo().toString());
        DataUsersTransactionDTO recebimentoData = new DataUsersTransactionDTO(recebimento.getNome_completo(), recebimento.getCpf(), recebimento.getTipo().toString());
        TransactionResponseDTO response = new TransactionResponseDTO(transaction.getId(), transaction.getValor(), Instant.now(), envioData, recebimentoData);


        when(userSerivceImpl.findUserJpaByCpf("1")).thenReturn(envio);
        when(userSerivceImpl.findUserJpaByCpf("2")).thenReturn(recebimento);
        when(verification.verificarTransacao(any())).thenReturn(false);


        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionRequestDTO request = new TransactionRequestDTO(new BigDecimal(1000),"1", "2");
            transactionService.transferir(request);
        });

        Assertions.assertEquals("Sua transação foi negada por motivos de segurança, tente novamente em alguns minutos", thrown.getMessage());

    }

}