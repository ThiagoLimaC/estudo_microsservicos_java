package br.com.thiagolima.api_udemy.client.response;

import br.com.thiagolima.api_udemy.dto.response.ProdutoResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorComProdutoResponse {
    private PessoaResponse pessoa;
    private List<ProdutoResponse> produtos;
}
