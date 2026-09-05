package br.com.thiagolima.api_udemy.controller;

import br.com.thiagolima.api_udemy.dto.request.CategoriaRequest;
import br.com.thiagolima.api_udemy.dto.response.CategoriaResponse;
import br.com.thiagolima.api_udemy.services.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obterTodos() {
        log.info("GET /api/v1/categorias");
        List<CategoriaResponse> response = categoriaService.obterTodos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obterPorId(@PathVariable Integer id) {
        log.info("GET /api/v1/categorias/{}", id);
        CategoriaResponse response = categoriaService.obterPorId(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> adicionar(@Valid @RequestBody CategoriaRequest request) {
        log.info("POST /api/v1/categorias");
        CategoriaResponse response = categoriaService.adicionar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequest request) {
        log.info("PUT /api/v1/categorias/{}", id);
        CategoriaResponse response = categoriaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        log.info("DELETE /api/v1/categorias/{}", id);
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
