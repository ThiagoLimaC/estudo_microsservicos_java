package br.com.thiagolima.api_udemy.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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
