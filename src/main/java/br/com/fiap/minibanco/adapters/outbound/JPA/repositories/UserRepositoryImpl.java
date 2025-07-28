package br.com.fiap.minibanco.adapters.outbound.JPA.repositories;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.core.user.ports.UserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
@AllArgsConstructor

@Service
public class UserRepositoryImpl implements UserRepositoryPort
{
    @Autowired
    private final JpaUserRepository jpaUserRepository;

    @Override
    public void registrar(UserJpa userJpa)
    {
        this.jpaUserRepository.save(userJpa);

    }

    @Override
    public UserDetails findByCpf(String cpf) {
        UserDetails user = this.jpaUserRepository.findByCpf(cpf);
        return user;
    }

    @Override
    public Optional<UserJpa> findUserJpaByCpf(String cpf) {
        if(this.jpaUserRepository.findUserJpaByCpf(cpf).isEmpty())
        {
            throw new RuntimeException();
        }
        else
        {
            return this.jpaUserRepository.findUserJpaByCpf(cpf);
        }
    }
}
