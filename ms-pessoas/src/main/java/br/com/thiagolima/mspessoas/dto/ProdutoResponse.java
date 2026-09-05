package br.com.thiagolima.mspessoas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponse {
    private Integer id;
    private String nome;
    private Integer quantidade;
    private Double valor;
    private String observacao;
    private LocalDate dataValidade;
    private String categoriaNome;
    private String fornecedorNome;
}

