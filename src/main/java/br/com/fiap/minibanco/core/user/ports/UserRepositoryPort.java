package br.com.fiap.minibanco.core.user.ports;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepositoryPort
{
    void registrar(UserJpa userJpa);
    UserDetails findByCpf(String cpf);
    Optional<UserJpa> findUserJpaByCpf(String cpf, boolean registro);
    Optional<UserJpa> findUserJpaByEmail(String email);
    void deleteUserJpaByCpf(String cpf);
    UserJpa updateUserJpa(UserJpa userJpa);

}
