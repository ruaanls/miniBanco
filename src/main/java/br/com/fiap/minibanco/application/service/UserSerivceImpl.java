package br.com.fiap.minibanco.application.service;
import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.adapters.outbound.security.TokenServicePort;
import br.com.fiap.minibanco.application.usecases.UserUsecases;
import br.com.fiap.minibanco.application.usecases.VerificationTransactionUsecases;
import br.com.fiap.minibanco.core.user.DTO.UserLoginDTO;
import br.com.fiap.minibanco.core.user.DTO.UserLoginResponseDTO;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import br.com.fiap.minibanco.core.user.DTO.UserResponseDTO;
import br.com.fiap.minibanco.core.user.ports.UserRepositoryPort;
import br.com.fiap.minibanco.infra.exception.EmailExistsException;
import br.com.fiap.minibanco.infra.exception.TransactionNotAllowedException;
import br.com.fiap.minibanco.infra.exception.UserExistsException;
import br.com.fiap.minibanco.infra.exception.UserNotFoundException;
import br.com.fiap.minibanco.utils.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if(userRepositoryPort.findUserJpaByCpf(registroDto.getCpf(), true).isPresent())
        {
            throw new UserExistsException();
        }
        if(this.userRepositoryPort.findUserJpaByEmail(registroDto.getEmail()).isPresent())
        {
            throw new EmailExistsException(registroDto.getEmail());
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

    @Override
    public UserJpa findUserJpaByCpf(String cpf) {
        if(this.userRepositoryPort.findUserJpaByCpf(cpf, false).isEmpty())
        {
            throw new UserNotFoundException(cpf);
        }
        else
        {
            return this.userRepositoryPort.findUserJpaByCpf(cpf, false).get();
        }
    }

    @Override
    public UserResponseDTO findUserResponseByCpf(String cpf) {
        if(this.userRepositoryPort.findUserJpaByCpf(cpf, false).isEmpty())
        {
            throw new UserNotFoundException(cpf);
        }
        else
        {
            validarAlteracao(cpf);
            UserResponseDTO userResponse =  this.userMapper.userToUserResponse(this.userRepositoryPort.findUserJpaByCpf(cpf, false).get());
            return userResponse;
        }
    }

    @Override
    @Transactional
    public void deleteUser(String cpf) {
        validarAlteracao(cpf);
        this.userRepositoryPort.deleteUserJpaByCpf(cpf);
    }

    @Override
    public UserResponseDTO updateUser(UserRegistroDto registroDto, String cpf) {

        validarAlteracao( cpf);
        Optional<UserJpa> userVelho = this.userRepositoryPort.findUserJpaByCpf(cpf, false);
        if(userVelho.isPresent())
        {
            String idAntigo = userVelho.get().getId();
            userVelho.get().setId(idAntigo);
            userVelho.get().setCpf(registroDto.getCpf());
            userVelho.get().setEmail(registroDto.getEmail());
            String senhaCriptografada = new BCryptPasswordEncoder().encode(registroDto.getSenha());
            userVelho.get().setSenha(senhaCriptografada);
            userVelho.get().setTipo(registroDto.getTipoConta());
            userVelho.get().setNome_completo(registroDto.getNome());

            UserJpa userJpa =  this.userRepositoryPort.updateUserJpa(userVelho.get());
            UserResponseDTO userResponse = this.userMapper.userToUserResponse(userJpa);
            return userResponse;
        }
        else
        {
            throw new UserNotFoundException();
        }

    }



    public void validarAlteracao( String cpf)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserJpa usuarioAutenticado = (UserJpa) authentication.getPrincipal();
        String cpfToken = usuarioAutenticado.getCpf();


        if(!cpfToken.equals(cpf))
        {
            throw new TransactionNotAllowedException("Você não pode realizar ações em contas que não sejam a sua, por favor realize ações em contas que tenham o mesmo cpf do seu login");
        }

    }


}
