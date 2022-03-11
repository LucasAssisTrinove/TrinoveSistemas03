package br.com.trinove.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.trinove.domain.Pedido;

public interface Emailservice {
	
	void sendOrdeConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
}
