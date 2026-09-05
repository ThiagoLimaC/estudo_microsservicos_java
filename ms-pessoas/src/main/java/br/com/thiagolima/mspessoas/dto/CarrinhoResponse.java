package br.com.thiagolima.mspessoas.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoResponse {
    private Integer pessoaId;
    private List<CarrinhoItemResponse> itens;
}
