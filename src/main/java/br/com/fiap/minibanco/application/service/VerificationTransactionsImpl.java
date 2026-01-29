package br.com.fiap.minibanco.application.service;

import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.UserJpa;
import br.com.fiap.minibanco.application.usecases.VerificationTransactionUsecases;
import br.com.fiap.minibanco.application.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.domain.ports.outbound.ExternalTransactionAuthorizationPort;
import br.com.fiap.minibanco.domain.service.TransactionDomainService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class VerificationTransactionsImpl implements VerificationTransactionUsecases {

    @Autowired
    private final UserSerivceImpl userSerivceImpl;

    @Autowired
    private final TransactionDomainService transactionDomainService;

    @Autowired
    private final ExternalTransactionAuthorizationPort externalTransactionAuthorizationPort;

    @Override
    public boolean verificarTransacao(TransactionRequestDTO request) {
        transactionDomainService.validarCpfRemetenteDiferenteDestinatario(request.getCpfEnvio(), request.getCpfRecebimento());

        UserJpa userEnvio = userSerivceImpl.findUserJpaByCpf(request.getCpfEnvio());
        UserJpa userRecebimento = userSerivceImpl.findUserJpaByCpf(request.getCpfRecebimento());

        transactionDomainService.validarSaldosNaoNulos(userEnvio.getSaldo(), userRecebimento.getSaldo());
        transactionDomainService.validarSaldoSuficiente(userEnvio.getSaldo(), request.getValor());

        return externalTransactionAuthorizationPort.isTransactionAuthorized();
    }

    @Override
    public void validarAutorizacaoTransacao(TransactionRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserJpa usuarioAutenticado = (UserJpa) authentication.getPrincipal();
        String cpfUsuario = usuarioAutenticado.getCpf();

        transactionDomainService.validarAutorizacaoTransacao(cpfUsuario, requestDTO.getCpfEnvio(), requestDTO.getCpfRecebimento());
    }
}
