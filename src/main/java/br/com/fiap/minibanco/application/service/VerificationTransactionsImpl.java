package br.com.fiap.minibanco.application.service;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.application.usecases.VerificationTransactionUsecases;
import br.com.fiap.minibanco.core.transactionals.DTO.AuthTransactionDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import br.com.fiap.minibanco.infra.config.restClient.ApiUrls;
import br.com.fiap.minibanco.infra.exception.BalanceInsufficientException;
import br.com.fiap.minibanco.infra.exception.OracleInputException;
import br.com.fiap.minibanco.infra.exception.SameCPFException;
import br.com.fiap.minibanco.infra.exception.TransactionNotAllowedException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@AllArgsConstructor
public class VerificationTransactionsImpl implements VerificationTransactionUsecases
{
    @Autowired
    private final UserSerivceImpl userSerivceImpl;

    @Override
    public boolean verificarTransacao(TransactionRequestDTO request) {


        if(request.getCpfEnvio().equals(request.getCpfRecebimento()))
        {
            throw new SameCPFException();
        }
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
            try{
                RestClient restClient = RestClient.builder()
                        .baseUrl(ApiUrls.BASE_URL_AUTH_TRANSACTIONS)
                        .build();
                AuthTransactionDTO response = restClient.get()
                        .uri(ApiUrls.URL_AUTH_TRANSACTIONS)
                        .retrieve()
                        .body(AuthTransactionDTO.class);
                if(response != null && response.getData().isAuthorization())
                {
                    return true;
                }
                else
                {
                    return false;
                }

            }catch (Exception e)
            {
                return false;
            }

        }

    }

    @Override
    public void validarAutorizacaoTransacao(TransactionRequestDTO requestDTO)
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




}
