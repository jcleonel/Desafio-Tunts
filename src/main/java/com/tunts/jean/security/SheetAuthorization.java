package com.tunts.jean.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.tunts.jean.constants.AppConstants;

public class SheetAuthorization {


	//Method for getting authorization and credentials to access the Google Sheets API
	private static Credential authorize() throws IOException, GeneralSecurityException {
		//Get the json file with the OAuth 2.0 client IDs
		InputStream in = SheetAuthorization.class.getResourceAsStream(AppConstants.CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Credentials not found: " + AppConstants.CREDENTIALS_FILE_PATH);
		}
		
		//Converts json with credentials into a GoogleClientSecrets object
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(AppConstants.JSON_FACTORY, new InputStreamReader(in));
		
		/* List of types of access to Google Drive
		 * The SPREADSHEETS scope allow see, edit, create, and delete your spreadsheets in Google Drive*/
		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
		
		/* Responsible for making the user connection and authorization flow and returns the URI with this OAuth 2 context.
		 * And it also returns an encrypted token with authorization and access credentials so you can access the App offline */
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), AppConstants.JSON_FACTORY, clientSecrets, scopes)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(AppConstants.TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		
		//Return credentials
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
				.authorize("client_id");

		return credential;
	}
	
	public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
		Credential credential = authorize();
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), AppConstants.JSON_FACTORY, credential)
				.setApplicationName(AppConstants.APPLICATION_NAME).build();
	}
}
