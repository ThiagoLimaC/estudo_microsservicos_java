package br.com.thiagolima.mspessoas.config;

import br.com.thiagolima.mspessoas.dto.PessoaRequest;
import br.com.thiagolima.mspessoas.entity.Pessoa;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // PessoaRequest -> Pessoa
        mapper.createTypeMap(PessoaRequest.class, Pessoa.class)
                .addMappings(m -> m.skip(Pessoa::setId))
                .addMappings(m -> m.skip(Pessoa::setDataCriacao))
                .addMappings(m -> m.skip(Pessoa::setDataAtualizacao));

        return mapper;
    }
}
