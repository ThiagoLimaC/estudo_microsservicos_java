package br.com.thiagolima.mspessoas.repository;

import br.com.thiagolima.mspessoas.entity.CarrinhoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarrinhoItemRepository extends JpaRepository<CarrinhoItem, Integer> {
    List<CarrinhoItem> findByPessoaId(Integer pessoaId);
}
