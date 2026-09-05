package br.com.thiagolima.mspessoas.service;

import br.com.thiagolima.mspessoas.client.ProdutoClient;
import br.com.thiagolima.mspessoas.dto.CarrinhoItemRequest;
import br.com.thiagolima.mspessoas.dto.CarrinhoItemResponse;
import br.com.thiagolima.mspessoas.dto.CarrinhoResponse;
import br.com.thiagolima.mspessoas.dto.ProdutoResponse;
import br.com.thiagolima.mspessoas.entity.CarrinhoItem;
import br.com.thiagolima.mspessoas.entity.Pessoa;
import br.com.thiagolima.mspessoas.exception.ResourceNotFoundException;
import br.com.thiagolima.mspessoas.repository.CarrinhoItemRepository;
import br.com.thiagolima.mspessoas.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoItemRepository carrinhoItemRepository;
    private final PessoaRepository pessoaRepository;
    private final ProdutoClient produtoClient;

    public void adicionarItem(Integer pessoaId, CarrinhoItemRequest request){
        log.info("Adicionando produto={} ao carrinho da pessoa={}", request.getProdutoId(), pessoaId);

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));

        produtoClient.obterPorId(request.getProdutoId());

        CarrinhoItem item = CarrinhoItem.builder()
                .pessoa(pessoa)
                .produtoId(request.getProdutoId())
                .quantidade(request.getQuantidade())
                .build();

        carrinhoItemRepository.save(item);
        log.info("Item adicionado ao carrinho da pessoa={}", pessoaId);
    }

    public CarrinhoResponse obterCarrinho(Integer pessoaId){
        log.info("Buscando carrinho da pessoa={}", pessoaId);

        pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));

        List<CarrinhoItem> itens = carrinhoItemRepository.findByPessoaId(pessoaId);

        List<CarrinhoItemResponse> itensResponse = itens.stream()
                .map(item -> {
                    ProdutoResponse produto = produtoClient.obterPorId(item.getProdutoId());
                    return CarrinhoItemResponse.builder()
                            .produtoId(item.getProdutoId())
                            .nomeProduto(produto.getNome())
                            .quantidade(item.getQuantidade())
                            .build();
                })
                .toList();
        return CarrinhoResponse.builder()
                .pessoaId(pessoaId)
                .itens(itensResponse)
                .build();
    }
}
