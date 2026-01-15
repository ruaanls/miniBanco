package br.com.fiap.minibanco.application.service;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;
import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.application.usecases.TransactionUsecases;
import br.com.fiap.minibanco.core.transactionals.DTO.AuthTransactionDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.DataUsersTransactionDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import br.com.fiap.minibanco.core.transactionals.ports.TransactionRepositoryPort;
import br.com.fiap.minibanco.core.user.ports.UserRepositoryPort;
import br.com.fiap.minibanco.infra.config.restClient.ApiUrls;
import br.com.fiap.minibanco.infra.exception.*;
import br.com.fiap.minibanco.utils.mapper.TransactionMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDateTime;
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

    @Autowired
    private final UserRepositoryPort userRepositoryPort;

    @Autowired
    private final VerificationTransactionsImpl verification;





    @Override
    @Transactional
    public TransactionResponseDTO transferir(TransactionRequestDTO transactionRequestDTO) {
        LocalDateTime dataHora = LocalDateTime.now();

        UserJpa userEnvio = userSerivceImpl.findUserJpaByCpf(transactionRequestDTO.getCpfEnvio());
        UserJpa userRecebimento = userSerivceImpl.findUserJpaByCpf(transactionRequestDTO.getCpfRecebimento());

        verification.validarAutorizacaoTransacao(transactionRequestDTO);
        if(verification.verificarTransacao(transactionRequestDTO) == false)
        {
            throw new TransactionNotAllowedException();
        }

        else
        {
            userEnvio.setSaldo(userEnvio.getSaldo().subtract(transactionRequestDTO.getValor()));
            userRecebimento.setSaldo(userRecebimento.getSaldo().add(transactionRequestDTO.getValor()));
            TransactionJPA transaction =  this.transactionMapper.requestToTransaction(transactionRequestDTO,userEnvio, userRecebimento, dataHora);
            try
            {
                this.userRepositoryPort.registrar(userEnvio);
                this.userRepositoryPort.registrar(userRecebimento);
                TransactionJPA transactionJPA = transactionRepository.transferir(transaction);
                DataUsersTransactionDTO envio = this.transactionMapper.userDtoToDataUsersTransactionDto(userEnvio);
                DataUsersTransactionDTO recebimento = this.transactionMapper.userDtoToDataUsersTransactionDto(userRecebimento);
                return this.transactionMapper.transactionToResponse(transactionJPA, envio, recebimento);
            }
            catch (Exception e)
            {

                throw new OracleInputException();
            }

        }

    }

    @Override
    public Page<TransactionResponseDTO> extrato(Pageable pageable, String cpf) {
        this.userSerivceImpl.validarAlteracao(cpf);
        Page<TransactionJPA> extrato = this.transactionRepository.findAllTransactionsJpaByCpf(cpf, pageable);
        return extrato.map(transaction -> {
            DataUsersTransactionDTO envio = this.transactionMapper.userDtoToDataUsersTransactionDto(transaction.getUsuarioEnvio());
            DataUsersTransactionDTO recebimento = this.transactionMapper.userDtoToDataUsersTransactionDto(transaction.getUsuarioRecebimento());
            return this.transactionMapper.transactionToResponse(transaction, envio, recebimento);
        });
    }



}
