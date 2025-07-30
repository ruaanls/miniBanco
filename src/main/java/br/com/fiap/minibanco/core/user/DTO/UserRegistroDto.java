package br.com.fiap.minibanco.core.user.DTO;

import br.com.fiap.minibanco.core.user.TipoConta;
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
public class UserRegistroDto
{
    @NotNull(message = "O campo NOME não deve ficar em branco! ")
    @NotBlank(message = "O campo NOME é obrigatório")
    private String nome;
    @NotNull(message = "O campo CPF não deve ficar em branco! ")
    @NotBlank(message = "O campo CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;
    @NotNull(message = "O campo TIPOCONTA não deve ficar em branco e é OBRIGATÓRIO! ")
    private TipoConta tipoConta;
    @NotNull(message = "O campo EMAIL não deve ficar em branco! ")
    @NotBlank(message = "O campo EMAIL é obrigatório")
    @Email(message = "Por favor digite um email válido!")
    private String email;
    @NotNull(message = "O campo SENHA não deve ficar em branco! ")
    @NotBlank(message = "O campo SENHA é obrigatório")
    @Size(min = 3, message = "A senha deve ter no mínimo 3 caracteres")
    private String senha;
    @NotNull(message = "O campo SALDO não deve ficar em branco! ")
    @DecimalMin(value = "0.0", inclusive = true, message = "O saldo deve ser maior ou igual a zero")
    @DecimalMax(value = "9999999999999", inclusive = false, message = "O saldo deve ser menor que 9.999.999.999.999, por favor digite um saldo válido")
    private BigDecimal saldo = BigDecimal.ZERO;

}
