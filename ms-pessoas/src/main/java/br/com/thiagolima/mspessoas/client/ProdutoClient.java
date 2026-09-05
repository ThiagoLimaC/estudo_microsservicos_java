package br.com.thiagolima.mspessoas.client;

import br.com.thiagolima.mspessoas.dto.ProdutoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-produto")
public interface ProdutoClient {

    @GetMapping("/api/v1/produtos/{id}")
    ProdutoResponse obterPorId(@PathVariable Integer id);
}
