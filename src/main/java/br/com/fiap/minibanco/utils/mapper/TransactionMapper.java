package br.com.fiap.minibanco.utils.mapper;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.TransactionJPA;
import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.core.transactionals.DTO.DataUsersTransactionDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface TransactionMapper
{

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "transaction.valor", target = "valor"),
            @Mapping(source = "usuarioEnvio", target = "usuarioEnvio"),
            @Mapping(source = "usuarioRecebimento", target = "usuarioRecebimento"),
            @Mapping(source = "dataHora", target = "data_hora_transacao"),
    })
    TransactionJPA requestToTransaction(TransactionRequestDTO transaction, UserJpa usuarioEnvio, UserJpa usuarioRecebimento, Instant dataHora);

    @Mappings({

            @Mapping(source = "userJpa.nome_completo", target = "nomeCompleto"),
            @Mapping(source = "userJpa.cpf", target = "cpf"),
            @Mapping(source = "userJpa.tipo", target = "tipo")

    })
    DataUsersTransactionDTO userDtoToDataUsersTransactionDto(UserJpa userJpa);

    @Mappings({
            @Mapping(source = "transaction.id", target = "id"),
            @Mapping(source = "transaction.valor", target = "valor"),
            @Mapping(source = "transaction.data_hora_transacao", target = "dataHora"),
            @Mapping(source = "usuarioEnvio", target = "usuarioEnvio"),
            @Mapping(source = "usuarioRecebimento", target = "usuarioRecebimento")
    })

    TransactionResponseDTO transactionToResponse(TransactionJPA transaction, DataUsersTransactionDTO usuarioEnvio, DataUsersTransactionDTO usuarioRecebimento);
}
