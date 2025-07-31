package br.com.fiap.minibanco.adapters.outbound.JPA.entities;

import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionJPA
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal valor;
    @ManyToOne
    @JoinColumn(name = "id_envio")
    private UserJpa usuarioEnvio;
    @ManyToOne
    @JoinColumn(name = "id_recebedor")
    private UserJpa usuarioRecebimento;
    private Instant data_hora_transacao;

    public TransactionJPA(Long id ,BigDecimal valor, UserJpa usuarioEnvio, UserJpa usuarioRecebimento)
    {
        this.id = id;
        this.valor = valor;
        this.data_hora_transacao = Instant.now();
        this.usuarioEnvio = usuarioEnvio;
        this.usuarioRecebimento = usuarioRecebimento;
    }
}
