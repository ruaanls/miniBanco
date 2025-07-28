package br.com.fiap.minibanco.application.usecases;

import br.com.fiap.minibanco.core.user.DTO.UserLoginDTO;
import br.com.fiap.minibanco.core.user.DTO.UserLoginResponseDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;

public interface UserUsecases
{
    void registrar(UserRegistroDto userRegistroDto);
    UserLoginResponseDTO login(UserLoginDTO userLoginDTO);
}
