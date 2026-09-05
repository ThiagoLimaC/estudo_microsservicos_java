package br.com.thiagolima.api_udemy.services;

import br.com.thiagolima.api_udemy.client.PessoaClient;
import br.com.thiagolima.api_udemy.client.response.FornecedorComProdutoResponse;
import br.com.thiagolima.api_udemy.client.response.PessoaResponse;
import br.com.thiagolima.api_udemy.dto.request.CategoriaRequest;
import br.com.thiagolima.api_udemy.dto.request.FornecedorRequest;
import br.com.thiagolima.api_udemy.dto.response.CategoriaResponse;
import br.com.thiagolima.api_udemy.dto.response.FornecedorResponse;
import br.com.thiagolima.api_udemy.dto.response.ProdutoResponse;
import br.com.thiagolima.api_udemy.entity.Categoria;
import br.com.thiagolima.api_udemy.entity.Fornecedor;
import br.com.thiagolima.api_udemy.entity.Produto;
import br.com.thiagolima.api_udemy.exception.ResourceNotFoundException;
import br.com.thiagolima.api_udemy.repository.FornecedorRepository;
import br.com.thiagolima.api_udemy.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final ModelMapper modelMapper;
    private final PessoaClient pessoaClient;

    /**
     * Metodo para retornar todos os fornecedores
     * @return lista de fornecedorResponse
     */
    public List<FornecedorResponse> obterTodos(){
        log.info("Buscando todos os forncedores");

        return fornecedorRepository.findAll().stream()
                .map(fornecedor -> modelMapper.map(fornecedor, FornecedorResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Metodo que retorna o fornecedor encontrada pelo seu id
     * @param id do fornecedor localizado
     * @return CategoriaResponse
     */
    public FornecedorResponse obterPorId(Integer id){
        log.info("Buscando fornecedor id={}", id);

        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forncedor não encontrado"));

        return modelMapper.map(fornecedor, FornecedorResponse.class);
    }

    public PessoaResponse obterPorCnpj(String cnpj){
        log.info("Buscando fornecedor cnpj={}", cnpj);

        return pessoaClient.obterPorCnpj(cnpj);
    }

    public FornecedorComProdutoResponse obterProdutosPorCnpj(String cnpj) {
        log.info("Buscando produtos do fornecedor cnpj={}", cnpj);

        // bato no serviço de usuario buscando pelo cnpj da pessoa
        PessoaResponse pessoa = obterPorCnpj(cnpj);

        // busco qual o fornecedor está associado aquele cnpj
        Fornecedor fornecedor = fornecedorRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        // busco os produtos que tem o id do fornecedor encontrado assossiado
        List<ProdutoResponse> produtos = produtoRepository.findByFornecedorId(fornecedor.getId())
                .stream()
                .map(p -> modelMapper.map(p, ProdutoResponse.class))
                .toList();

        return FornecedorComProdutoResponse.builder()
                .pessoa(pessoa)
                .produtos(produtos)
                .build();
    }

    /**
     * Metodo para adicionar o fornecedor ao banco
     * @param request objeto fornecedor a ser inserido
     * @return FornecedorResponse
     */
    public FornecedorResponse adicionar(FornecedorRequest request){
        log.info("Adicionando fornecedor: {}", request.getNome());

        Fornecedor fornecedor = modelMapper.map(request, Fornecedor.class);

        fornecedor = fornecedorRepository.save(fornecedor);

        return modelMapper.map(fornecedor, FornecedorResponse.class);
    }

    /**
     * Metodo para deletar o fornecedor por id
     * @param id do fornecedor a ser deletado
     */
    public void deletar(Integer id){
        log.info("Deletando o forncedor de id={}", id);

        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        fornecedorRepository.deleteById(id);

        log.info("Fornecedor id={} deletado", id);
    }

    public FornecedorResponse atualizar(Integer id,FornecedorRequest request){

        log.info("Atualizando fornecedor id={}", id);

        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        fornecedor.setNome(request.getNome());
        fornecedor.setCnpj(request.getCnpj());
        fornecedor.setEmail(request.getEmail());
        fornecedor.setTelefone(request.getTelefone());

        fornecedor = fornecedorRepository.save(fornecedor);
        log.info("Fornecedor id={} atualizado", id);

        return modelMapper.map(fornecedor, FornecedorResponse.class);
    }
}
