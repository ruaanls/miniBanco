package br.com.fiap.minibanco.domain.ports.outbound;

/**
 * Port para autorização externa de transação (ex.: API externa).
 * A implementação (adapter) fica na camada de infraestrutura.
 */
public interface ExternalTransactionAuthorizationPort {

    /**
     * Consulta se a transação está autorizada pelo serviço externo.
     *
     * @return true se autorizada, false caso contrário ou em caso de falha na comunicação
     */
    boolean isTransactionAuthorized();
}
