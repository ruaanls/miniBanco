package br.com.fiap.minibanco.infra.config.security;

import br.com.fiap.minibanco.adapters.outbound.security.TokenServicePort;
import br.com.fiap.minibanco.core.user.ports.UserRepositoryPort;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenServicePort tokenService;

    @Autowired
    UserRepositoryPort userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        var token = this.recoverToken(request);
        if(token != null)
        {
            try
            {
                // Validar o token JWT e retornar o login do usuário
                var login = tokenService.validateToken(token);
                // Com o login do usuário procurar no banco de dados e receber um user do tipo UserDetails
                UserDetails user = userRepository.findByCpf(login);
                // Autenticar o usuário com a classe especial do spring security UsernamePasswordAuthenticationToken, os parametros são sempre dessa forma
                // Ela serve para autenticar o usuário e saber quais roles ele tem acesso
                var authentication = new UsernamePasswordAuthenticationToken(user, null , user.getAuthorities());
                // Salvando o token da autenticação no contexto do spring
                SecurityContextHolder.getContext().setAuthentication(authentication);


            }
            catch (TokenExpiredException e) {
                // Token expirado - será capturado pelo AuthenticationEntryPoint
                request.setAttribute("exception", e);
            } catch (JWTVerificationException e) {
                // Token inválido - será capturado pelo AuthenticationEntryPoint
                System.out.println("JWTVerificationException capturada: " + e.getMessage());
                request.setAttribute("exception", e);
            } catch (Exception e) {
                // Outros erros de token
                request.setAttribute("exception", e);
            }
        }
        //Manda para o próximo filtro (Filtro padrão do spring security -- Classe Spring configuration)
        filterChain.doFilter(request, response);

    }

    private String recoverToken(HttpServletRequest request)
    {
        var token = request.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer "))
        {
            return null;
        }
        return token.replace("Bearer ", "");
    }
}