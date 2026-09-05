package br.com.thiagolima.mspessoas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PessoaResponse {
    private Integer id;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
}
