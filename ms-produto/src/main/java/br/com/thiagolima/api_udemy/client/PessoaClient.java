package br.com.thiagolima.api_udemy.client;

import br.com.thiagolima.api_udemy.client.response.PessoaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-pessoas")
public interface PessoaClient {

    @GetMapping("api/v1/pessoas/cnpj/{cnpj}")
    PessoaResponse obterPorCnpj(@PathVariable String cnpj);
}
