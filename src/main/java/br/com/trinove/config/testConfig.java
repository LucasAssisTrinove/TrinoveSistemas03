package br.com.trinove.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.trinove.services.DBService;

@Configuration
@Profile("test")
public class testConfig {
	
	@Autowired
	private DBService dbService;

	@Bean
	public boolean instantiateDataabase() throws ParseException {
		dbService.instantiateTestDatabase();
		return true;
	}
}
