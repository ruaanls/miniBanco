package br.com.fiap.minibanco.application.service;
import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.adapters.outbound.security.TokenServicePort;
import br.com.fiap.minibanco.application.usecases.UserUsecases;
import br.com.fiap.minibanco.core.user.DTO.UserLoginDTO;
import br.com.fiap.minibanco.core.user.DTO.UserLoginResponseDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import br.com.fiap.minibanco.core.user.ports.UserRepositoryPort;
import br.com.fiap.minibanco.utils.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor

@Service
public class UserSerivceImpl implements UserUsecases
{
    @Autowired
    private final UserRepositoryPort userRepositoryPort;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private TokenServicePort tokenService;

    @Override
    public void registrar(UserRegistroDto registroDto) {
        if(userRepositoryPort.findByCpf(registroDto.getCpf())!= null)
        {
            throw new RuntimeException();
        }
        String senhaCriptografada = new BCryptPasswordEncoder().encode(registroDto.getSenha());
        UserJpa userJpa = this.userMapper.requestToUser(registroDto, senhaCriptografada);
        this.userRepositoryPort.registrar(userJpa);
    }

    @Override
    public UserLoginResponseDTO login(UserLoginDTO loginDTO) {
        var usernamePassoword = new UsernamePasswordAuthenticationToken(loginDTO.getCpf(), loginDTO.getSenha());
        var auth = this.authenticationManager.authenticate(usernamePassoword);
        UserJpa login = (UserJpa) auth.getPrincipal();
        var token = this.tokenService.generateToken(login);
        return this.userMapper.requestToLogin(token);
    }
}
