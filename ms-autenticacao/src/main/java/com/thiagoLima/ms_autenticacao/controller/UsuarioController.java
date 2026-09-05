package com.thiagoLima.ms_autenticacao.controller;

import com.thiagoLima.ms_autenticacao.dto.LoginRequest;
import com.thiagoLima.ms_autenticacao.dto.LoginResponse;
import com.thiagoLima.ms_autenticacao.dto.TokenResponse;
import com.thiagoLima.ms_autenticacao.dto.UsuarioResponse;
import com.thiagoLima.ms_autenticacao.model.Usuario;
import com.thiagoLima.ms_autenticacao.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> obterTodos() {
        return usuarioService.obterTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obter(@PathVariable("id") Long id){
        return usuarioService.obterPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario salvar (@RequestBody Usuario usuario){
        Usuario usuarioSalvo = usuarioService.adicionar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo).getBody();
    }


//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest request){
//        LoginResponse response = usuarioService.logar(request.getEmail(), request.getSenha());
//        return ResponseEntity.ok(response).getBody();
//    }

    @PostMapping("/autenticacao/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse response = usuarioService.autenticar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/autenticacao/perfil")
    public ResponseEntity<UsuarioResponse> obterPerfil(@AuthenticationPrincipal Usuario usuario) {
        UsuarioResponse response = usuarioService.obterPerfil(usuario);
        return ResponseEntity.ok(response);
    }
}
