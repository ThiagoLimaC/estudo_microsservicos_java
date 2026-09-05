package br.com.thiagolima.api_udemy.mapper;

import br.com.thiagolima.api_udemy.dto.request.ProdutoRequest;
import br.com.thiagolima.api_udemy.dto.response.ProdutoResponse;
import br.com.thiagolima.api_udemy.entity.Categoria;
import br.com.thiagolima.api_udemy.entity.Fornecedor;
import br.com.thiagolima.api_udemy.entity.Produto;
import br.com.thiagolima.api_udemy.exception.ResourceNotFoundException;
import br.com.thiagolima.api_udemy.repository.CategoriaRepository;
import br.com.thiagolima.api_udemy.repository.FornecedorRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProdutoMapper {

    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    public Produto toProduto(ProdutoRequest request){

        Categoria categoria = resolveCategoria(request.getCategoriaId());
        Fornecedor fornecedor = resolveFornecedor(request.getFornecedorId());

        return Produto.builder()
                .nome(request.getNome())
                .quantidade(request.getQuantidade())
                .valor(request.getValor())
                .observacao(request.getObservacao())
                .dataValidade(request.getDataValidade())
                .categoria(categoria)
                .fornecedor(fornecedor)
                .build();
    }

    public ProdutoResponse toProdutoResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidade(),
                produto.getValor(),
                produto.getObservacao(),
                produto.getDataValidade(),
                produto.getCategoria().getNome(),
                produto.getFornecedor().getNome()
        );
    }

    public Categoria resolveCategoria(Integer categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria com id " + categoriaId + " não encontrada"
                ));
    }

    public Fornecedor resolveFornecedor(Integer fornecedorId) {
        return fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fornecedor com id " + fornecedorId + " não encontrado"
                ));
    }
}
