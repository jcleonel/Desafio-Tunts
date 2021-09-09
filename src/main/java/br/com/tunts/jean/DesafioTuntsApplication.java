package br.com.tunts.jean;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.tunts.jean.service.SheetService;

@SpringBootApplication
public class DesafioTuntsApplication implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(DesafioTuntsApplication.class);

	public static void main(String[] args) {				
		SpringApplication.run(DesafioTuntsApplication.class, args);
		Scanner sc = new Scanner(System.in);
		logger.info("Application started!");
		try {
			
			SheetService sheet = new SheetService();
			sheet.getSheet();	
			int op = -1;
			while (op != 0) {
				System.out.println("");
				System.out.println(" ########################################## ");
				System.out.println("   Menu:");
				System.out.println("   1 - Load spreadsheet ");
				System.out.println("   2 - Clean spreadsheet ");
				System.out.println("   0 - Exit ");
				System.out.print("   Enter the desired option: ");
				op = sc.nextInt();
				System.out.println(" ########################################## ");
				System.out.println("");
				if (op == 2) {
					sheet.cleanSheet();
				} else if (op == 1) {
					sheet.setSheet();
				} else {
					op = 0;
				}
			}			
			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (GeneralSecurityException e) {			
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
		
		logger.info("Application finished!");
	}

	@Override
	public void run() {
		
		
	}

}
