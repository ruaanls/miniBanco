package br.com.fiap.minibanco.application.usecases;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.core.user.DTO.UserLoginDTO;
import br.com.fiap.minibanco.core.user.DTO.UserLoginResponseDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import br.com.fiap.minibanco.core.user.DTO.UserResponseDTO;

public interface UserUsecases
{
    void registrar(UserRegistroDto userRegistroDto);
    UserLoginResponseDTO login(UserLoginDTO userLoginDTO);
    UserJpa findUserJpaByCpf(String cpf);
    UserResponseDTO findUserResponseByCpf(String cpf);
}
