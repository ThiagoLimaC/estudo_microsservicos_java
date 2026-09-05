package br.com.thiagolima.api_udemy.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FiltroGenerico {
    // Paginação
    private Integer page = 0;
    private Integer size = 10;
    private String sort = "id"; // campo padrão
    private String direction = "ASC"; // ou DESC
}
