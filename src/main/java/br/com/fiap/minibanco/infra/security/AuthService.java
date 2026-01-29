package br.com.fiap.minibanco.infra.security;


import br.com.fiap.minibanco.domain.ports.outbound.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService
{
    @Autowired
    private UserRepositoryPort jpaUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserDetails user = jpaUserRepository.findByCpf(username);
        if(user == null)
        {
            throw new InternalAuthenticationServiceException("Usuário não encontrado: " + username);
        }
        return user;

    }
}
