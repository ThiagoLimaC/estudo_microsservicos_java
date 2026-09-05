package br.com.thiagolima.mspessoas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PessoaRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Cnpj é obrigatório")
    private String cnpj;

    @NotNull(message = "Email é obrigatório")
    private String email;

    @NotNull(message = "Telefone é obrigatório")
    private String telefone;
}
