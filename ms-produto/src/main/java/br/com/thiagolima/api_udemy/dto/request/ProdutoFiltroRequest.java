package br.com.thiagolima.api_udemy.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoFiltroRequest extends FiltroGenerico {
    private String nome;
    private Double precoMinimo;
    private Double precoMaximo;
    private Integer categoriaId;
    private Integer fornecedorId;
    private Integer quantidadeMenor;


    public Pageable criarPageable() {
        Sort.Direction direction = Sort.Direction.fromString(this.getDirection());

        return PageRequest.of(
                this.getPage(),
                this.getSize(),
                Sort.by(direction, this.getSort())
        );
    }
}
