package br.com.fiap.minibanco.domain.service;

import br.com.fiap.minibanco.infra.exception.TransactionNotAllowedException;
import org.springframework.stereotype.Service;


@Service
public class UserAuthorizationDomainService {


    public void validarAlteracaoPropriaConta(String cpfUsuarioAutenticado, String cpfAlvo) {
        if (cpfUsuarioAutenticado == null || !cpfUsuarioAutenticado.equals(cpfAlvo)) {
            throw new TransactionNotAllowedException(
                    "Você não pode realizar ações em contas que não sejam a sua, por favor realize ações em contas que tenham o mesmo cpf do seu login");
        }
    }
}
