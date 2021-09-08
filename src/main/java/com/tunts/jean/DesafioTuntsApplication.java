package com.tunts.jean;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tunts.jean.service.SheetService;

@SpringBootApplication
public class DesafioTuntsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioTuntsApplication.class, args);
		
		try {
			SheetService sheet = new SheetService();
			sheet.getSheet();
			sheet.setSheet();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (GeneralSecurityException e) {			
			e.printStackTrace();
		}
	}

}