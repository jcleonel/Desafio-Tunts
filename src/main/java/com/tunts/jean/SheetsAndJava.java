package com.tunts.jean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.tunts.jean.entities.Student;

public class SheetsAndJava {
	private static Sheets sheetService;
	private static String APPLICATION_NAME = "DesafioTunts";
	private static String SPREADSHEET_ID = "1xoNwa8hgPpEpNMeq7XvIclVqXZfuesamHbS_F9I7Pgk";
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "token_uri";

	private static Credential authorize() throws IOException, GeneralSecurityException {

		InputStream in = SheetsAndJava.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Credentials not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, scopes)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();

		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
				.authorize("client_id");

		return credential;

	}

	public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
		Credential credential = authorize();
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	@SuppressWarnings("unused")
	public static void main(String... args) throws IOException, GeneralSecurityException {
		sheetService = getSheetsService();
		String range = "A4:F27";

		ValueRange response = sheetService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();

		List<List<Object>> values = response.getValues();

		List<Student> students = new ArrayList<>();
		if (values == null || values.isEmpty()) {
			System.out.println("No data found");
		} else {
			for (List<? extends Object> row : values) {

				String enrollmentId = (String) row.get(0);
				String name = (String) row.get(1);
				String absence = (String) row.get(2);
				String testGrade1 = (String) row.get(3);
				String testGrade2 = (String) row.get(4);
				String testGrade3 = (String) row.get(5);

				students.add(new Student(Long.valueOf(enrollmentId), name, Integer.valueOf(absence),
						Double.valueOf(testGrade1), Double.valueOf(testGrade2), Double.valueOf(testGrade3)));

			}
		}

		System.out.println(students);

		

		for (int i = 4; i < 28; i++) {
			ValueRange situation = new ValueRange().setValues(Arrays.asList(Arrays.asList(students.get(i - 4).getSituation())));
			UpdateValuesResponse result = sheetService.spreadsheets().values().update(SPREADSHEET_ID, "G" + i, situation)
					.setValueInputOption("RAW").execute();
			
			ValueRange gradeFinalApproval = new ValueRange().setValues(Arrays.asList(Arrays.asList(students.get(i - 4).getGradeFinalApproval())));
			result = sheetService.spreadsheets().values().update(SPREADSHEET_ID, "H" + i, gradeFinalApproval)
					.setValueInputOption("RAW").execute();
		}

	}

}
