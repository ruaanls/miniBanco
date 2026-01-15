package br.com.fiap.minibanco.adapters.outbound.JPA.entities;

import br.com.fiap.minibanco.core.user.DTO.UserRegistroDto;
import br.com.fiap.minibanco.core.user.TipoConta;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_jpa")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@DynamicUpdate
public class UserJpa implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nome_completo;
    private String cpf;
    @Enumerated(EnumType.STRING)
    private TipoConta tipo;
    private String email;
    private String senha;
    private BigDecimal saldo;


    public UserJpa(UserRegistroDto data)
    {
        this.nome_completo = data.getNome();
        this.cpf = data.getCpf();
        this.tipo = data.getTipoConta();
        this.email = data.getEmail();
        this.senha = data.getSenha();
        this.saldo = data.getSaldo();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.tipo.equals(TipoConta.COMUM))
        {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        else
        {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.cpf;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
