package br.com.fiap.minibanco.utils.mapper;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.core.user.DTO.UserLoginDTO;
import br.com.fiap.minibanco.core.user.DTO.UserLoginResponseDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import br.com.fiap.minibanco.core.user.DTO.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "registerDTO.nome", target = "nome_completo"),
            @Mapping(source = "registerDTO.cpf", target = "cpf"),
            @Mapping(source = "registerDTO.tipoConta", target = "tipo"),
            @Mapping(source = "registerDTO.email", target = "email"),
            @Mapping(source = "passwordEncrypted", target = "senha"),
            @Mapping(source = "registerDTO.saldo", target = "saldo")
    })
    UserJpa requestToUser(UserRegistroDto registerDTO, String passwordEncrypted);

    @Mappings({
            @Mapping(source = "token", target = "token")
    })
    UserLoginResponseDTO requestToLogin(String token);

    @Mappings({
            @Mapping(source = "userJpa.id", target = "id"),
            @Mapping(source = "userJpa.nome_completo", target = "nome_completo"),
            @Mapping(source = "userJpa.cpf", target = "cpf"),
            @Mapping(source = "userJpa.tipo", target = "tipo"),
            @Mapping(source = "userJpa.email", target = "email"),
            @Mapping(source = "userJpa.senha", target = "senha"),
            @Mapping(source = "userJpa.saldo", target = "saldo"),
            @Mapping(target = "authorities", ignore = true)
    })
    UserJpa optionalToUser(UserJpa userJpa);

    @Mappings({
            @Mapping(source = "userJpa.nome_completo", target = "nome"),
            @Mapping(source = "userJpa.cpf", target = "cpf"),
            @Mapping(source = "userJpa.tipo", target = "tipoConta"),
            @Mapping(source = "userJpa.saldo", target = "saldo"),
    })
    UserResponseDTO userToUserResponse(UserJpa userJpa);
}
