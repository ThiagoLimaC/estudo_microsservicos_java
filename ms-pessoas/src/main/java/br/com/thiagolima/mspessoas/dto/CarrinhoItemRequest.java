package br.com.thiagolima.mspessoas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoItemRequest {

    @NotNull(message = "Produto é obrigatório")
    private Integer produtoId;

    @NotNull(message = "Quantidade é obrigatório")
    private Integer quantidade;
}
