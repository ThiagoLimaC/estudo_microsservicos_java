package br.com.thiagolima.mspessoas.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoItemResponse {
    private Integer produtoId;
    private String nomeProduto;
    private Integer quantidade;
}
