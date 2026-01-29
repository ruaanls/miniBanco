package br.com.fiap.minibanco.application.service;

import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.TransactionJPA;
import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.UserJpa;
import br.com.fiap.minibanco.application.usecases.TransactionUsecases;
import br.com.fiap.minibanco.application.DTO.DataUsersTransactionDTO;
import br.com.fiap.minibanco.application.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.application.DTO.TransactionResponseDTO;
import br.com.fiap.minibanco.domain.ports.outbound.TransactionRepositoryPort;
import br.com.fiap.minibanco.domain.ports.outbound.UserRepositoryPort;
import br.com.fiap.minibanco.infra.exception.*;
import br.com.fiap.minibanco.utils.mapper.TransactionMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

                throw new OracleInputException(e);
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
