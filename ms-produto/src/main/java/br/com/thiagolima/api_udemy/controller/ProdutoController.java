package br.com.thiagolima.api_udemy.controller;

import br.com.thiagolima.api_udemy.dto.request.ProdutoFiltroRequest;
import br.com.thiagolima.api_udemy.services.ProdutoService;
import br.com.thiagolima.api_udemy.dto.request.ProdutoRequest;
import br.com.thiagolima.api_udemy.dto.response.ProdutoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/produtos")
@Slf4j
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping("/filtro")
    public ResponseEntity<Page<ProdutoResponse>> obterTodosFiltro(
            @ModelAttribute ProdutoFiltroRequest filtro) {
        log.info("GET /api/v1/produtosFiltro");
        return ResponseEntity.ok(produtoService.filtrar(filtro));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> obterTodosFiltro() {
        log.info("GET /api/v1/produtos");
        return ResponseEntity.ok(produtoService.obterTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> obterPorId(@PathVariable Integer id) {
        log.info("GET /api/v1/produtos/{}", id);
        ProdutoResponse response = produtoService.obterPorId(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> adicionar(@Valid @RequestBody ProdutoRequest produtoReq){
        log.info("POST /api/v1/produtos");
        ProdutoResponse response = produtoService.adicionar(produtoReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id){
        log.info("DELETE /api/v1/produtos");
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProdutoRequest produtoReq) {

        log.info("PUT api/v1/produtos");
        ProdutoResponse response = produtoService.atualizar(id, produtoReq);
        return ResponseEntity.ok(response);
    }
}
