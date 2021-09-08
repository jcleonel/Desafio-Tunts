package br.com.tunts.jean.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import br.com.tunts.jean.constants.Constants;

public class SheetAuthorization {
	
	private static final Logger logger = LoggerFactory.getLogger(SheetAuthorization.class);	

	//Method for getting authorization and credentials to access the Google Sheets API
	private static Credential authorize() throws IOException, GeneralSecurityException {
		
		//Get the json file with the OAuth 2.0 client IDs		
		logger.info("Loading credentials...");
		InputStream in = SheetAuthorization.class.getResourceAsStream(Constants.CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Credentials not found: " + Constants.CREDENTIALS_FILE_PATH);
		}
		
		//Converts json with credentials into a GoogleClientSecrets object
		logger.info("Converting JSON credentials to GoogleClientSecrets...");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(Constants.JSON_FACTORY, new InputStreamReader(in));
		
		/* List of types of access to Google Drive
		 * The SPREADSHEETS scope allow see, edit, create, and delete your spreadsheets in Google Drive*/
		logger.info("Putting SPREADSHEETS in the request scope...");
		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
		
		/* Responsible for making the user connection and authorization flow and returns the URI with this OAuth 2 context.
		 * And it also returns an encrypted token with authorization and access credentials so you can access the App offline */
		logger.info("Returning flow...");
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), Constants.JSON_FACTORY, clientSecrets, scopes)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(Constants.TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		
		//Return credentials
		logger.info("Returning credential...");
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
				.authorize("client_id");

		return credential;
	}
	
	public static Sheets getSheetsService() throws IOException, GeneralSecurityException {	
		Credential credential = authorize();
		logger.info("Returning Sheets...");
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), Constants.JSON_FACTORY, credential)
				.setApplicationName(Constants.APPLICATION_NAME).build();
	}

}
