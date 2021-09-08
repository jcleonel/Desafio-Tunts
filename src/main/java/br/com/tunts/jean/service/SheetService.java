package br.com.tunts.jean.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import br.com.tunts.jean.constants.Constants;
import br.com.tunts.jean.entities.Student;
import br.com.tunts.jean.security.SheetAuthorization;

public class SheetService {
	
	private static final Logger logger = LoggerFactory.getLogger(SheetService.class);	
	
	private List<Student> students = new ArrayList<>();
	private Sheets sheetService;	
	
	public SheetService() throws IOException, GeneralSecurityException {
		//Getting access credentials
		this.sheetService = SheetAuthorization.getSheetsService();
	}

	public void getSheet() throws IOException, GeneralSecurityException {
		String range = Constants.RANGE;
		logger.info("Loading spreadsheet rows...");
		ValueRange response = sheetService.spreadsheets().values().get(Constants.SPREADSHEET_ID, range).execute();
		List<List<Object>> values = response.getValues();

		//Converting spreadsheet rows to student objects
		if (values == null || values.isEmpty()) {
			System.out.println("No data found");
		} else {
			logger.info("Converting spreadsheet rows to student objects...");
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
		
	}
	
	@SuppressWarnings("unused")
	//Saving student's situation in the spreadsheet
	public void setSheet() throws IOException {
		for (int i = 0; i < students.size(); i++) {
			logger.info("Saving student " + students.get(i).getName() +"'s situation in the spreadsheet...");
			ValueRange situation = new ValueRange().setValues(Arrays.asList(Arrays.asList(students.get(i).getSituation())));
			UpdateValuesResponse result = sheetService.spreadsheets().values().update(Constants.SPREADSHEET_ID, "G" + (i + 4), situation)
					.setValueInputOption("RAW").execute();
				
			ValueRange gradeFinalApproval = new ValueRange().setValues(Arrays.asList(Arrays.asList(students.get(i).getGradeFinalApproval())));
			result = sheetService.spreadsheets().values().update(Constants.SPREADSHEET_ID, "H" + (i + 4), gradeFinalApproval)
					.setValueInputOption("RAW").execute();			
		}
	}
	
}
