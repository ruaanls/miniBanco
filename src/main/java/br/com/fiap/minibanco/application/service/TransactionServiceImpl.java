package br.com.fiap.minibanco.application.service;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;
import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.application.usecases.TransactionUsecases;
import br.com.fiap.minibanco.core.transactionals.DTO.DataUsersTransactionDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import br.com.fiap.minibanco.core.transactionals.ports.TransactionRepositoryPort;
import br.com.fiap.minibanco.utils.mapper.TransactionMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionUsecases
{
    @Autowired
    private final TransactionRepositoryPort transactionRepository;

    @Autowired
    private final UserSerivceImpl userSerivceImpl;

    @Autowired
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponseDTO transferir(TransactionRequestDTO transactionRequestDTO) {
        Instant dataHora = Instant.now().truncatedTo(ChronoUnit.MINUTES);
        UserJpa userEnvio = userSerivceImpl.findUserJpaByCpf(transactionRequestDTO.getCpfEnvio());
        UserJpa userRecebimento = userSerivceImpl.findUserJpaByCpf(transactionRequestDTO.getCpfRecebimento());

        if(verificarTransacao(transactionRequestDTO) == false)
        {
            throw new RuntimeException();
        }
        else
        {
            userEnvio.setSaldo(userEnvio.getSaldo().subtract(transactionRequestDTO.getValor()));
            userRecebimento.setSaldo(userRecebimento.getSaldo().add(transactionRequestDTO.getValor()));
            TransactionJPA transaction =  this.transactionMapper.requestToTransaction(transactionRequestDTO,userEnvio, userRecebimento, dataHora);
            TransactionJPA transactionJPA = transactionRepository.transferir(transaction);
            DataUsersTransactionDTO envio = this.transactionMapper.userDtoToDataUsersTransactionDto(userEnvio);
            DataUsersTransactionDTO recebimento = this.transactionMapper.userDtoToDataUsersTransactionDto(userRecebimento);
            return this.transactionMapper.transactionToResponse(transactionJPA, envio, recebimento);
        }

    }


    @Override
    public boolean verificarTransacao(TransactionRequestDTO request) {
        UserJpa userEnvio = userSerivceImpl.findUserJpaByCpf(request.getCpfEnvio());
        UserJpa userRecebimento = userSerivceImpl.findUserJpaByCpf(request.getCpfRecebimento());
        if(userRecebimento.getSaldo() == null || userEnvio.getSaldo() == null)
        {
            throw new RuntimeException();
        }
        else if(userEnvio.getSaldo().compareTo(request.getValor()) < 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
