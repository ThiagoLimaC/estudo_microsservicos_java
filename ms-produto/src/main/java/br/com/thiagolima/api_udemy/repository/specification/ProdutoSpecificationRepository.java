package br.com.thiagolima.api_udemy.repository.specification;

import br.com.thiagolima.api_udemy.dto.request.ProdutoFiltroRequest;
import br.com.thiagolima.api_udemy.dto.response.ProdutoResponse;
import br.com.thiagolima.api_udemy.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface ProdutoSpecificationRepository {

    Page<ProdutoResponse> paginate(ProdutoFiltroRequest filtro);

    Page<ProdutoResponse> buscarComCriteria(
            Specification<Produto> spec,
            Pageable pageable
    );
}
