package br.com.trinove.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.trinove.domain.Cliente;
import br.com.trinove.domain.ItemPedido;
import br.com.trinove.domain.PagamentoComBoleto;
import br.com.trinove.domain.Pedido;
import br.com.trinove.domain.enums.EstadoPagamento;
import br.com.trinove.repositories.ClienteRepository;
import br.com.trinove.repositories.ItemPedidoRepository;
import br.com.trinove.repositories.PagamentoRepository;
import br.com.trinove.repositories.PedidoRepository;
import br.com.trinove.repositories.ProdutoRepository;
import br.com.trinove.security.UserSS;
import br.com.trinove.services.exception.AuthorizationException;
import br.com.trinove.services.exception.ObjectNotFoundException;


@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private Boletoservice Boletoservice;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteservice;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private Emailservice emailService;
	
	
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
		}
	
	//@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstate(new Date());
		obj.setCliente(clienteservice.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			Boletoservice.preencherPagamentoComBoleto(pagto, obj.getInstate());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrdeConfirmationEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}		
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction),orderBy);
		Cliente clinete =  clienteservice.find(user.getID());
		return repo.findByCliente(clinete, pageRequest);
		
	}

}
