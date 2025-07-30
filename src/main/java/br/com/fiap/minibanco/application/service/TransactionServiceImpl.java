package br.com.fiap.minibanco.application.service;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;
import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.application.usecases.TransactionUsecases;
import br.com.fiap.minibanco.core.transactionals.DTO.DataUsersTransactionDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import br.com.fiap.minibanco.core.transactionals.ports.TransactionRepositoryPort;
import br.com.fiap.minibanco.core.user.ports.UserRepositoryPort;
import br.com.fiap.minibanco.infra.exception.*;
import br.com.fiap.minibanco.utils.mapper.TransactionMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public TransactionResponseDTO transferir(TransactionRequestDTO transactionRequestDTO) {
        Instant dataHora = Instant.now().truncatedTo(ChronoUnit.MINUTES);
        if(transactionRequestDTO.getCpfEnvio().equals(transactionRequestDTO.getCpfRecebimento()))
        {
            throw new SameCPFException();
        }
        UserJpa userEnvio = userSerivceImpl.findUserJpaByCpf(transactionRequestDTO.getCpfEnvio());
        UserJpa userRecebimento = userSerivceImpl.findUserJpaByCpf(transactionRequestDTO.getCpfRecebimento());

        validarAutorizacaoTransacao(transactionRequestDTO);
        if(verificarTransacao(transactionRequestDTO) == false)
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

    private void validarAutorizacaoTransacao(TransactionRequestDTO requestDTO)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserJpa usuarioAutenticado = (UserJpa) authentication.getPrincipal();
        String cpfUsuario = usuarioAutenticado.getCpf();

        boolean isRementente = cpfUsuario.equals(requestDTO.getCpfEnvio());
        boolean isDestinatario = cpfUsuario.equals(requestDTO.getCpfRecebimento());

        if(!isRementente && !isDestinatario)
        {
            throw new TransactionNotAllowedException("Usuário não autorizado a realizar essa transação, você só pode fazer transações onde você seja o remetente ou destinatário. ");
        }

        if(!isRementente && isDestinatario)
        {
            throw new TransactionNotAllowedException("Você não pode iniciar uma transação como destinatário, apenas o remetente pode iniciar");
        }

    }


    private boolean verificarTransacao(TransactionRequestDTO request) {
        UserJpa userEnvio = userSerivceImpl.findUserJpaByCpf(request.getCpfEnvio());
        UserJpa userRecebimento = userSerivceImpl.findUserJpaByCpf(request.getCpfRecebimento());
        if(userRecebimento.getSaldo() == null || userEnvio.getSaldo() == null)
        {
            throw new OracleInputException();
        }
        else if(userEnvio.getSaldo().compareTo(request.getValor()) < 0)
        {
            throw new BalanceInsufficientException();
        }
        else
        {
            return true;
        }
    }
}
