package br.com.fiap.minibanco.application.usecases;

import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.UserJpa;
import br.com.fiap.minibanco.application.DTO.UserLoginDTO;
import br.com.fiap.minibanco.application.DTO.UserLoginResponseDTO;
import br.com.fiap.minibanco.application.DTO.UserRegistroDto;
import br.com.fiap.minibanco.application.DTO.UserResponseDTO;

public interface UserUsecases
{
    void registrar(UserRegistroDto userRegistroDto);
    UserLoginResponseDTO login(UserLoginDTO userLoginDTO);
    UserJpa findUserJpaByCpf(String cpf);
    UserResponseDTO findUserResponseByCpf(String cpf);
    void deleteUser(String cpf);
    UserResponseDTO updateUser(UserRegistroDto userRegistroDto, String cpf);

}
