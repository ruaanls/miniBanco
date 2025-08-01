package br.com.fiap.minibanco.adapters.outbound.JPA.repositories;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.core.user.ports.UserRepositoryPort;
import br.com.fiap.minibanco.infra.exception.EmailExistsException;
import br.com.fiap.minibanco.infra.exception.UserNotFoundException;
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
        if(user == null)
        {
            throw new UserNotFoundException(cpf);
        }
        return user;
    }

    @Override
    public Optional<UserJpa> findUserJpaByCpf(String cpf, boolean registro) {
        if(registro)
        {
            if(this.jpaUserRepository.findUserJpaByCpf(cpf).isPresent())
            {
                return this.jpaUserRepository.findUserJpaByCpf(cpf);
            }
            else
            {
                return Optional.empty();
            }
        }
        else
        {
            if(this.jpaUserRepository.findUserJpaByCpf(cpf).isEmpty())
            {
                throw new UserNotFoundException(cpf);
            }
            else
            {
                return this.jpaUserRepository.findUserJpaByCpf(cpf);
            }
        }

    }

    @Override
    public Optional<UserJpa> findUserJpaByEmail(String email) {
        if(this.jpaUserRepository.findUserJpaByEmail(email).isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            throw new EmailExistsException(email);
        }
    }
}
