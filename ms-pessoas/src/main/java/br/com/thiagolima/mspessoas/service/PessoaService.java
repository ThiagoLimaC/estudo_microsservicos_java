package br.com.thiagolima.mspessoas.service;

import br.com.thiagolima.mspessoas.dto.PessoaRequest;
import br.com.thiagolima.mspessoas.dto.PessoaResponse;
import br.com.thiagolima.mspessoas.entity.Pessoa;
import br.com.thiagolima.mspessoas.exception.ResourceNotFoundException;
import br.com.thiagolima.mspessoas.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    public PessoaResponse obterPorCnpj(String cnpj){
        log.info("Buscando pessoa por cnpj={}", cnpj);

        Pessoa pessoa = pessoaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada para o CNPJ: " + cnpj));

        return modelMapper.map(pessoa, PessoaResponse.class);
    }

    public List<PessoaResponse> obterTodos(){
        log.info("Buscando todos as pessoas");

        return pessoaRepository.findAll().stream()
                .map(pessoa -> modelMapper.map(pessoa, PessoaResponse.class))
                .collect(Collectors.toList());
    }

    public PessoaResponse obterPorId(Integer id){
        log.info("Buscando pessoa id={}", id);

        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrado"));

        return modelMapper.map(pessoa, PessoaResponse.class);
    }

    public PessoaResponse adicionar(PessoaRequest request){
        log.info("Adicionando pessoa: {}", request.getNome());

        Pessoa pessoa = modelMapper.map(request, Pessoa.class);

        pessoa = pessoaRepository.save(pessoa);

        return modelMapper.map(pessoa, PessoaResponse.class);
    }

    public void deletar(Integer id){
        log.info("Deletando a pessoa de id={}", id);

        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));

        pessoaRepository.deleteById(id);

        log.info("Pessoa id={} deletada", id);
    }

    public PessoaResponse atualizar(Integer id, PessoaRequest request){

        log.info("Atualizando pessoa id={}", id);

        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));

        pessoa.setNome(request.getNome());
        pessoa.setCnpj(request.getCnpj());
        pessoa.setEmail(request.getEmail());
        pessoa.setTelefone(request.getTelefone());

        pessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa id={} atualizada", id);

        return modelMapper.map(pessoa, PessoaResponse.class);
    }
}
