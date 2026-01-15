package br.com.fiap.minibanco.adapters.outbound.JPA.entities;

import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_jpa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionJPA
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "TRANSACTION_JPA_SEQ", allocationSize = 1)
    private Long id;
    private BigDecimal valor;
    @ManyToOne
    @JoinColumn(name = "id_envio")
    private UserJpa usuarioEnvio;
    @ManyToOne
    @JoinColumn(name = "id_recebedor")
    private UserJpa usuarioRecebimento;
    @Column(name = "data_hora_transacao", columnDefinition = "TIMESTAMP")
    private LocalDateTime data_hora_transacao;

    public TransactionJPA(Long id ,BigDecimal valor, UserJpa usuarioEnvio, UserJpa usuarioRecebimento)
    {
        this.id = id;
        this.valor = valor;
        this.data_hora_transacao = LocalDateTime.now();
        this.usuarioEnvio = usuarioEnvio;
        this.usuarioRecebimento = usuarioRecebimento;
    }
}
