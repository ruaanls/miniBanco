package br.com.fiap.minibanco.application.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionRequestDTO
{
    @NotNull(message = "O campo SALDO não deve ficar em branco! ")
    @DecimalMin(value = "0.0", inclusive = true, message = "O saldo deve ser maior ou igual a zero")
    @DecimalMax(value = "9999999999999", inclusive = false, message = "O saldo deve ser menor que 9.999.999.999.999, por favor digite um saldo válido")
    private BigDecimal valor;
    @NotNull(message = "O campo CPF não deve ficar em branco! ")
    @NotBlank(message = "O campo CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpfEnvio;
    @NotNull(message = "O campo CPF não deve ficar em branco! ")
    @NotBlank(message = "O campo CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpfRecebimento;

}
