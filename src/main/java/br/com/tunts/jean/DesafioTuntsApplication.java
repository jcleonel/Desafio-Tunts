package br.com.tunts.jean;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.tunts.jean.service.SheetService;

@SpringBootApplication
public class DesafioTuntsApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(DesafioTuntsApplication.class);

	public static void main(String[] args) {				
		SpringApplication.run(DesafioTuntsApplication.class, args);
		
		logger.info("Application started!");
		try {
			SheetService sheet = new SheetService();
			sheet.getSheet();
			sheet.setSheet();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (GeneralSecurityException e) {			
			e.printStackTrace();
		}	
		logger.info("Application finished!");
	}

}
