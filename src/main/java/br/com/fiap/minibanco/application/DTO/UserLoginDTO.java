package br.com.fiap.minibanco.application.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginDTO
{
    @NotNull(message = "O campo CPF não deve ficar em branco! ")
    @NotBlank(message = "O campo CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;
    @NotNull(message = "O campo SENHA não deve ficar em branco! ")
    @NotBlank(message = "O campo SENHA é obrigatório")
    @Size(min = 3, message = "A senha deve ter no mínimo 3 caracteres")
    private String senha;
}
