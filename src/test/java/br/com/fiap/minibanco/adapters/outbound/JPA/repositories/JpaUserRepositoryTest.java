package br.com.fiap.minibanco.adapters.outbound.JPA.repositories;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;
import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import br.com.fiap.minibanco.core.user.TipoConta;
import br.com.fiap.minibanco.core.user.ports.UserRepositoryPort;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test") //Usar application-test.properties
class JpaUserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    JpaUserRepository userRepositoryPort;



    @Test
    @DisplayName("Devolver usuário através do CPF com sucesso")
    void findUserJpaByCpf()
    {
        String cpf = "123.456.789-00";
        UserJpa data = new UserJpa(null,"Ruan Lima","123.456.789-00", TipoConta.COMUM,"teste@gmail.com","senha123", new BigDecimal(100));
        this.entityManager.persist(data);

        Optional<UserJpa> resultado = this.userRepositoryPort.findUserJpaByCpf(cpf);
        assertTrue(resultado.isPresent());
        assertEquals(cpf, resultado.get().getCpf());

    }

    @Test
    @DisplayName("Não encontrar o usuário passando cpf inexistente")
    void findUserJpaByCpfError()
    {
        String cpf = "123.456.789-00";
        UserJpa data = new UserJpa(null,"Pedro Martins","123.456.789-01", TipoConta.LOJISTAS,"teste2@gmail.com","senha789", new BigDecimal(500));

        Optional<UserJpa> resultado = this.userRepositoryPort.findUserJpaByCpf(cpf);
        assertTrue(resultado.isEmpty());
    }


    @Test
    @DisplayName("Devolver usuário através do EMAIL com sucesso")
    void findUserJpaByEmail()
    {
        String email = "teste2@gmail.com";
        UserJpa data = new UserJpa(null,"Ruan Lima","123.456.789-01", TipoConta.LOJISTAS,"teste2@gmail.com","senha789", new BigDecimal(500));
        this.entityManager.persist(data);

        Optional<UserJpa> resultado = this.userRepositoryPort.findUserJpaByEmail(email);
        assertTrue(resultado.isPresent());
    }

    @Test
    @DisplayName("Não encontrar o usuário passando email inexistente")
    void findUserJpaByEmailError()
    {
        String email = "teste80@gmail.com";
        UserJpa data = new UserJpa(null,"Pedro Martins","123.456.789-01", TipoConta.LOJISTAS,"teste800@gmail.com","senha789", new BigDecimal(500));

        Optional<UserJpa> resultado = this.userRepositoryPort.findUserJpaByCpf(email);
        assertTrue(resultado.isEmpty());
    }



    private UserJpa createUser(UserRegistroDto userDto)
    {
        UserJpa userJpa = new UserJpa(userDto);
        this.entityManager.persist(userJpa);
        return userJpa;
    }
}