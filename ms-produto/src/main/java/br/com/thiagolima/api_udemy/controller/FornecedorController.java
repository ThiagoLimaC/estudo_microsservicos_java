package br.com.thiagolima.api_udemy.controller;

import br.com.thiagolima.api_udemy.client.response.FornecedorComProdutoResponse;
import br.com.thiagolima.api_udemy.client.response.PessoaResponse;
import br.com.thiagolima.api_udemy.dto.request.CategoriaRequest;
import br.com.thiagolima.api_udemy.dto.request.FornecedorRequest;
import br.com.thiagolima.api_udemy.dto.response.CategoriaResponse;
import br.com.thiagolima.api_udemy.dto.response.FornecedorResponse;
import br.com.thiagolima.api_udemy.services.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @GetMapping("/cnpj/{cnpj}/produtos")
    public ResponseEntity<FornecedorComProdutoResponse> obterProdutosPorCnpj(@PathVariable String cnpj) {
        log.info("GET /api/v1/fornecedores/cnpj/{}/produtos", cnpj);
        FornecedorComProdutoResponse response = fornecedorService.obterProdutosPorCnpj(cnpj);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponse>> obterTodos() {
        log.info("GET /api/v1/fornecedores");
        List<FornecedorResponse> response = fornecedorService.obterTodos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponse> obterPorId(@PathVariable Integer id) {
        log.info("GET /api/v1/fornecedores/{}", id);
        FornecedorResponse response = fornecedorService.obterPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<PessoaResponse> obterPorCnpj(@PathVariable String cnpj) {
        log.info("GET /api/v1/fornecedores/{}", cnpj);
        PessoaResponse response = fornecedorService.obterPorCnpj(cnpj);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<FornecedorResponse> adicionar(@Valid @RequestBody FornecedorRequest request) {
        log.info("POST /api/v1/fornecedores");
        FornecedorResponse response = fornecedorService.adicionar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody FornecedorRequest request) {
        log.info("PUT /api/v1/fornecedores/{}", id);
        FornecedorResponse response = fornecedorService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        log.info("DELETE /api/v1/fornecedores/{}", id);
        fornecedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
