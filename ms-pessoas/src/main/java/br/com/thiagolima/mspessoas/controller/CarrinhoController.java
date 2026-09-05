package br.com.thiagolima.mspessoas.controller;

import br.com.thiagolima.mspessoas.dto.CarrinhoItemRequest;
import br.com.thiagolima.mspessoas.dto.CarrinhoResponse;
import br.com.thiagolima.mspessoas.service.CarrinhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/pessoas/{pessoaId}/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {
    private final CarrinhoService carrinhoService;

    @PostMapping
    public ResponseEntity<Void> adicionarItem(
            @PathVariable Integer pessoaId,
            @Valid @RequestBody CarrinhoItemRequest request) {
        log.info("POST /api/v1/pessoas/{}/carrinho", pessoaId);
        carrinhoService.adicionarItem(pessoaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<CarrinhoResponse> obterCarrinho(@PathVariable Integer pessoaId) {
        log.info("GET /api/v1/pessoas/{}/carrinho", pessoaId);
        return ResponseEntity.ok(carrinhoService.obterCarrinho(pessoaId));
    }
}
