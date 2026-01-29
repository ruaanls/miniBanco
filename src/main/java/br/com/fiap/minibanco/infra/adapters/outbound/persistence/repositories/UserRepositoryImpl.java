package br.com.fiap.minibanco.infra.adapters.outbound.persistence.repositories;

import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.UserJpa;
import br.com.fiap.minibanco.domain.ports.outbound.UserRepositoryPort;
import br.com.fiap.minibanco.infra.exception.EmailExistsException;
import br.com.fiap.minibanco.infra.exception.UserNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserRepositoryImpl implements UserRepositoryPort
{
    private final JpaUserRepository jpaUserRepository;
    private final EntityManager entityManager;


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

    @Override

    public void deleteUserJpaByCpf(String cpf) {
        if(this.findUserJpaByCpf(cpf, false).isEmpty())
        {
            throw new UserNotFoundException();
        }
        else
        {
            this.jpaUserRepository.deleteUserJpaByCpf(cpf);
            entityManager.flush();
            entityManager.clear();

        }
    }

    @Override
    public UserJpa updateUserJpa(UserJpa userJpa) {
        if(userJpa.getCpf().isEmpty())
        {
            throw new UserNotFoundException();
        }
        else
        {
            return this.jpaUserRepository.save(userJpa);
        }

    }
}
