package br.com.thiagolima.api_udemy.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProdutoRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser > 0")
    private Integer quantidade;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private Double valor;

    private String observacao;

    @NotNull(message = "Data de validade é obrigatória")
    @FutureOrPresent(message = "Data deve ser presente ou futura")
    private LocalDate dataValidade;

    @NotNull(message = "Categoria é obrigatório")
    private Integer categoriaId;

    @NotNull(message = "Fornecedor é obrigatório")
    private Integer fornecedorId;
}
