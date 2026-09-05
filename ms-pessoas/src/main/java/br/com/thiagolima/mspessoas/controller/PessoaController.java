package br.com.thiagolima.mspessoas.controller;

import br.com.thiagolima.mspessoas.dto.PessoaRequest;
import br.com.thiagolima.mspessoas.dto.PessoaResponse;
import br.com.thiagolima.mspessoas.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<PessoaResponse> obterPorCnpj(@PathVariable String cnpj) {
        log.info("GET /api/v1/pessoas/cnpj/{}", cnpj);
        PessoaResponse response = pessoaService.obterPorCnpj(cnpj);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponse>> obterTodos() {
        log.info("GET /api/v1/pessoas");
        List<PessoaResponse> response = pessoaService.obterTodos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> obterPorId(@PathVariable Integer id) {
        log.info("GET /api/v1/pessoas/{}", id);
        PessoaResponse response = pessoaService.obterPorId(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PessoaResponse> adicionar(@Valid @RequestBody PessoaRequest request) {
        log.info("POST /api/v1/pessoas");
        PessoaResponse response = pessoaService.adicionar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PessoaRequest request) {
        log.info("PUT /api/v1/pessoas/{}", id);
        PessoaResponse response = pessoaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        log.info("DELETE /api/v1/pessoas/{}", id);
        pessoaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
