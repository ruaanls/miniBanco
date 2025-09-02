package br.com.fiap.minibanco.adapters.outbound.JPA.repositories;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface JpaUserRepository extends JpaRepository<UserJpa, String>
{
    UserDetails findByCpf(String cpf);
    Optional<UserJpa> findUserJpaByCpf(String cpf);
    Optional<UserJpa> findUserJpaByEmail(String email);
    void deleteUserJpaByCpf(String cpf);
}
