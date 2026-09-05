package br.com.thiagolima.api_udemy.services;

import br.com.thiagolima.api_udemy.dto.request.ProdutoFiltroRequest;
import br.com.thiagolima.api_udemy.entity.Categoria;
import br.com.thiagolima.api_udemy.entity.Fornecedor;
import br.com.thiagolima.api_udemy.entity.Produto;
import br.com.thiagolima.api_udemy.mapper.ProdutoMapper;
import br.com.thiagolima.api_udemy.exception.ResourceNotFoundException;
import br.com.thiagolima.api_udemy.repository.ProdutoRepository;
import br.com.thiagolima.api_udemy.dto.request.ProdutoRequest;
import br.com.thiagolima.api_udemy.dto.response.ProdutoResponse;
import br.com.thiagolima.api_udemy.repository.specification.ProdutoSpecificationRepository;
import br.com.thiagolima.api_udemy.repository.specification.ProdutoSpecificationRepositoryImpl;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoSpecificationRepository produtoSpecificationRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;


    public Page<ProdutoResponse> filtrar(ProdutoFiltroRequest filtro) {

        log.info("Filtrando produtos - nome: [{}], preço: [{}-{}]",
                filtro.getNome(), filtro.getPrecoMinimo(), filtro.getPrecoMaximo());


        Specification<Produto> spec = Specification.where(null);

        if (filtro.getNome() != null && !filtro.getNome().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
        }

        if (filtro.getPrecoMinimo() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("valor"), filtro.getPrecoMinimo()));
        }

        if (filtro.getPrecoMaximo() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("valor"), filtro.getPrecoMaximo()));
        }

        if (filtro.getCategoriaId() != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Produto, Categoria> join = root.join("categoria", JoinType.LEFT);
                return cb.equal(join.get("id"), filtro.getCategoriaId());
            });
        }

        if (filtro.getFornecedorId() != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Produto, Fornecedor> join = root.join("fornecedor", JoinType.LEFT);
                return cb.equal(join.get("id"), filtro.getFornecedorId());
            });
        }

        return produtoSpecificationRepository.buscarComCriteria(spec, filtro.criarPageable());
    }

    // region CRUD
    /**
     * Retorna a lista completa de produtos
     * @return todos os produtos
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponse> obterTodos(){
        log.info("Buscando produtos");

        return produtoRepository.findAll()
                .stream()
                .map(produtoMapper::toProdutoResponse)
                .toList();
    }

    /**
     * Metodo que retorna o produto encontrado pelo seu Id.
     * @param id do produto que será localizado.
     * @return Retorna um dto do tipo ProdutoResponse.
     */
    @Transactional(readOnly = true)
    public ProdutoResponse obterPorId(Integer id){
        log.info("Buscando produto id={}", id);

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return produtoMapper.toProdutoResponse(produto);
    }

    /**
     * Metodo para adicionar produto no banco.
     * @param request que será adicionado
     * @return Retorna o produto que foi adicionado no banco
     */
    @Transactional
    public ProdutoResponse adicionar(ProdutoRequest request) {
        log.info("Adicionando produto: {}", request.getNome());

        Produto produto = produtoMapper.toProduto(request);

        produto = produtoRepository.save(produto);

        return produtoMapper.toProdutoResponse(produto);
    }

    /**
     * Metodo para deletar o produto por Id.
     * @param id do produto a ser deletado
     */
    @Transactional
    public void deletar(Integer id){

        log.info("Deletando produto de id={}", id);

        produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        produtoRepository.deleteById(id);

        log.info("Produto id={} deletado", id);
    }

    /**
     * Metodo para atualizar o produto na lista.
     * @param request que será atualizado.
     * @param id do produto.
     * @return Retorna o produto após atualizar a lista.
     */
    @Transactional
    public ProdutoResponse atualizar(Integer id, ProdutoRequest request) {

        log.info("Atualizando produto id={}", id);

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        produto.setNome(request.getNome());
        produto.setQuantidade(request.getQuantidade());
        produto.setValor(request.getValor());
        produto.setObservacao(request.getObservacao());
        produto.setDataValidade(request.getDataValidade());
        produto.setCategoria(produtoMapper.resolveCategoria(request.getCategoriaId()));
        produto.setFornecedor(produtoMapper.resolveFornecedor(request.getFornecedorId()));

        produto = produtoRepository.save(produto);
        log.info("Produto id={} atualizado", id);

        return produtoMapper.toProdutoResponse(produto);
    }
    // endregion
}
