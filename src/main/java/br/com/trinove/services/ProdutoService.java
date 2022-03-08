package br.com.trinove.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.trinove.domain.Categoria;
import br.com.trinove.domain.Produto;
import br.com.trinove.repositories.CategoriaRepository;
import br.com.trinove.repositories.ProdutoRepository;
import br.com.trinove.services.exception.ObjectNotFoundException;


@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	

	
	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
		}
	
	public Page<Produto> search(String nome, List<Integer> ids,Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction),orderBy);
		List<Categoria> categoria = categoriaRepository.findAllById(ids);
		return repo.findDistincByNomeContainingAndCategoriasIn(nome, categoria, pageRequest);
		
		//return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);	
		
	}

}
