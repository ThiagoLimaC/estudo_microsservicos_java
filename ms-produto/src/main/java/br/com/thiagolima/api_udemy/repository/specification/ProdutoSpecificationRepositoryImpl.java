package br.com.thiagolima.api_udemy.repository.specification;

import br.com.thiagolima.api_udemy.dto.request.ProdutoFiltroRequest;
import br.com.thiagolima.api_udemy.dto.response.ProdutoResponse;
import br.com.thiagolima.api_udemy.entity.Categoria;
import br.com.thiagolima.api_udemy.entity.Fornecedor;
import br.com.thiagolima.api_udemy.entity.Produto;
import br.com.thiagolima.api_udemy.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ProdutoSpecificationRepositoryImpl implements ProdutoSpecificationRepository {

    @PersistenceContext
    private EntityManager em;

    private  final ProdutoRepository repository;

    public Page<ProdutoResponse> paginate (ProdutoFiltroRequest filtro) {

        Specification<Produto> spec = Specification.allOf();

        Pageable pageable = filtro.criarPageable();

        return buscarComCriteria(spec, pageable);
    }

    @Override
    public Page<ProdutoResponse> buscarComCriteria(Specification<Produto> spec, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Produto> root = query.from(Produto.class);

        Join<Produto, Categoria> categoriaJoin = root.join("categoria", JoinType.LEFT);
        Join<Produto, Fornecedor> fornecedorJoin = root.join("fornecedor", JoinType.LEFT);

        query.multiselect(
                root.get("id").alias("id"),
                root.get("nome").alias("nome"),
                root.get("quantidade").alias("quantidade"),
                root.get("valor").alias("valor"),
                root.get("observacao").alias("observacao"),
                root.get("dataValidade").alias("dataValidade"),
                categoriaJoin.get("nome").alias("categoriaNome"),
                fornecedorJoin.get("nome").alias("fornecedorNome")
        );

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, cb);
            if (predicate != null) {
                query.where(predicate);
            }
        }

        TypedQuery<Tuple> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<ProdutoResponse> content = typedQuery.getResultList()
                .stream()
                .map( tuple ->ProdutoResponse.builder()
                        .id(tuple.get("id", Integer.class))
                        .nome(tuple.get("nome", String.class))
                        .quantidade(tuple.get("quantidade", Integer.class))
                        .valor(tuple.get("valor", Double.class))
                        .observacao(tuple.get("observacao", String.class))
                        .dataValidade(tuple.get("dataValidade", LocalDate.class))
                        .categoriaNome(tuple.get("categoriaNome", String.class))
                        .fornecedorNome(tuple.get("fornecedorNome", String.class))
                        .build()
                )
                .toList();


        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Produto> countRoot = countQuery.from(Produto.class);

        countQuery.select(cb.count(countRoot));

        if (spec != null) {
            Predicate countPredicate = spec.toPredicate(countRoot, countQuery, cb);
            if (countPredicate != null) {
                countQuery.where(countPredicate);
            }
        }

        countQuery.distinct(true);

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}
