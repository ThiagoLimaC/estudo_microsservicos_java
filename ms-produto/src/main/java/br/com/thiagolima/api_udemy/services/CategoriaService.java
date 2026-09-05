package br.com.thiagolima.api_udemy.services;

import br.com.thiagolima.api_udemy.dto.request.CategoriaRequest;
import br.com.thiagolima.api_udemy.dto.request.ProdutoRequest;
import br.com.thiagolima.api_udemy.dto.response.CategoriaResponse;
import br.com.thiagolima.api_udemy.dto.response.ProdutoResponse;
import br.com.thiagolima.api_udemy.entity.Categoria;
import br.com.thiagolima.api_udemy.exception.ResourceNotFoundException;
import br.com.thiagolima.api_udemy.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    /**
     * Metodo para retornar todas as categorias
     * @return lista de categoriaResponse
     */
    public List<CategoriaResponse> obterTodos(){
        log.info("Buscando todas as categorias");

        return categoriaRepository.findAll().stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Metodo que retorna a categoria encontrada pelo seu id
     * @param id da categoria localizada
     * @return CategoriaResponse
     */
    public CategoriaResponse obterPorId(Integer id){
        log.info("Buscando categoria id={}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        return modelMapper.map(categoria, CategoriaResponse.class);
    }

    /**
     * Metodo para adicionar a categoria ao banco
     * @param request objeto categoria a ser inserido
     * @return CategoriaResponse
     */
    public CategoriaResponse adicionar(CategoriaRequest request){
        log.info("Adicionando categoria: {}", request.getNome());

        Categoria categoria = modelMapper.map(request, Categoria.class);

        categoria = categoriaRepository.save(categoria);

        return modelMapper.map(categoria, CategoriaResponse.class);
    }

    /**
     * Metodo para deletar a categoria por id
     * @param id da categoria a ser deletada
     */
    public void deletar(Integer id){
        log.info("Deletando a categoria de id={}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        categoriaRepository.deleteById(id);

        log.info("Categoria id={} deletado", id);
    }

    /**
     * Metodo para atualizar uma categoria
     * @param id da categoria
     * @param request categoria a ser atualizada
     * @return CateogiraResponse
     */
    public CategoriaResponse atualizar(Integer id,CategoriaRequest request){

        log.info("Atualizando categoria id={}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        categoria.setNome(request.getNome());
        categoria.setDescricao(request.getDescricao());

        categoria = categoriaRepository.save(categoria);
        log.info("Categoria id={} atualizada", id);

        return modelMapper.map(categoria, CategoriaResponse.class);
    }
}
