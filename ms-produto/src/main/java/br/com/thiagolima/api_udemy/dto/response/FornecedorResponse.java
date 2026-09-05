package br.com.thiagolima.api_udemy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FornecedorResponse {

    private Integer id;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;

}
