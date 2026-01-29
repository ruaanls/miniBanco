package br.com.fiap.minibanco.domain.service;

import br.com.fiap.minibanco.infra.exception.BalanceInsufficientException;
import br.com.fiap.minibanco.infra.exception.OracleInputException;
import br.com.fiap.minibanco.infra.exception.SameCPFException;
import br.com.fiap.minibanco.infra.exception.TransactionNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class TransactionDomainService {


    public void validarCpfRemetenteDiferenteDestinatario(String cpfEnvio, String cpfRecebimento) {
        if (cpfEnvio != null && cpfEnvio.equals(cpfRecebimento)) {
            throw new SameCPFException();
        }
    }


    public void validarSaldosNaoNulos(BigDecimal saldoRemetente, BigDecimal saldoDestinatario) {
        if (saldoRemetente == null || saldoDestinatario == null) {
            throw new OracleInputException();
        }
    }


    public void validarSaldoSuficiente(BigDecimal saldoRemetente, BigDecimal valor) {
        if (saldoRemetente.compareTo(valor) < 0) {
            throw new BalanceInsufficientException();
        }
    }


    public void validarAutorizacaoTransacao(String cpfUsuarioAutenticado, String cpfEnvio, String cpfRecebimento) {
        boolean isRemetente = cpfUsuarioAutenticado.equals(cpfEnvio);
        boolean isDestinatario = cpfUsuarioAutenticado.equals(cpfRecebimento);

        if (!isRemetente && !isDestinatario) {
            throw new TransactionNotAllowedException(
                    "Usuário não autorizado a realizar essa transação, você só pode fazer transações onde você seja o remetente ou destinatário. ");
        }

        if (!isRemetente && isDestinatario) {
            throw new TransactionNotAllowedException(
                    "Você não pode iniciar uma transação como destinatário, apenas o remetente pode iniciar");
        }
    }


    public SaldosAtualizados aplicarTransferencia(BigDecimal saldoRemetente, BigDecimal saldoDestinatario, BigDecimal valor) {
        BigDecimal novoSaldoRemetente = saldoRemetente.subtract(valor);
        BigDecimal novoSaldoDestinatario = saldoDestinatario.add(valor);
        return new SaldosAtualizados(novoSaldoRemetente, novoSaldoDestinatario);
    }

    public record SaldosAtualizados(BigDecimal novoSaldoRemetente, BigDecimal novoSaldoDestinatario) {}
}
