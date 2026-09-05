package br.com.thiagolima.api_udemy.config;

import br.com.thiagolima.api_udemy.dto.request.CategoriaRequest;
import br.com.thiagolima.api_udemy.dto.request.FornecedorRequest;
import br.com.thiagolima.api_udemy.entity.Categoria;
import br.com.thiagolima.api_udemy.entity.Fornecedor;
import br.com.thiagolima.api_udemy.entity.Produto;
import br.com.thiagolima.api_udemy.dto.request.ProdutoRequest;
import br.com.thiagolima.api_udemy.dto.response.ProdutoResponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // FornecedorRequest -> Fornecedor
        mapper.createTypeMap(FornecedorRequest.class, Fornecedor.class)
                .addMappings(m -> m.skip(Fornecedor::setId))
                .addMappings(m -> m.skip(Fornecedor::setDataCriacao))
                .addMappings(m -> m.skip(Fornecedor::setDataAtualizacao));

        // CategoriaRequest -> Categoria
        mapper.createTypeMap(CategoriaRequest.class, Categoria.class)
                .addMappings(mapping -> mapping.skip(Categoria::setId))
                .addMappings(mapping -> mapping.skip(Categoria::setDataCriacao))
                .addMappings(mapping -> mapping.skip(Categoria::setDataAtualizacao));

        return mapper;
    }
}
