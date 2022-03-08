package br.com.trinove.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.trinove.domain.PagamentoComBoleto;

@Service
public class Boletoservice {
	
	public void preencherPagamentoComBoleto (PagamentoComBoleto pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimeno(cal.getTime());
	}

}
