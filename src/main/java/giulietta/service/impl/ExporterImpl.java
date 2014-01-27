package giulietta.service.impl;

import giulietta.config.Config;
import giulietta.model.Answer;
import giulietta.model.Item;
import giulietta.model.LiveSession;
import giulietta.model.Question;
import giulietta.model.Scenario;
import giulietta.service.ClassifierHelper;
import giulietta.service.Context;
import giulietta.service.api.Exporter;
import giulietta.service.api.Loader;
import giulietta.service.api.Statistics;
import giulietta.service.impl.LoaderImpl.InvalidScenarioException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExporterImpl implements Exporter {

	Loader loader;
	Statistics statistics;

	public ExporterImpl(Loader loader,Statistics statistics){
		this.loader=loader;
		this.statistics=statistics;
	}

	@Override
	public void export(){
		List<LiveSession> sessions = loader.loadAllSessions();
		List<Question> questions = statistics.getQuestionsList(sessions);

		FileOutputStream out;
		try {
			out = new FileOutputStream(Context.getProperty(Config.GIULIETTA_EXPORT_DIR)+File.separator+"output_total.xls");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		// create a new workbook
		Workbook workBook = new HSSFWorkbook();
		// create a new sheet
		Sheet sheet = workBook.createSheet();
		workBook.setSheetName(0, "All questions" );

		Row row = null;
		Cell cell = null;


		row = sheet.createRow(0);
		for (int i = 1;i<4;i++){
			cell=row.createCell(i);
			cell.setCellValue(" Reponse "+i);
		}
		int i_rownum;
		for (i_rownum = (short) 1; i_rownum <= questions.size(); i_rownum++)
		{
			row = sheet.createRow(i_rownum);
			cell=row.createCell(0);
			cell.setCellValue("Question " + i_rownum);
			for (Integer cellnum =  1; cellnum < 4; cellnum++)
			{
				cell = row.createCell(cellnum);
				if (questions.get(i_rownum-1).getAnswers().containsKey(cellnum)){

					cell.setCellValue(questions.get(i_rownum-1).getAnswers().get(cellnum));
					System.out.println(questions.get(i_rownum-1).getAnswers().get(cellnum));
				} else {
					cell.setCellValue(0);
				}
			}
		}
		try {
			workBook.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void completeExport(){
		FileOutputStream out;
		try {
			out = new FileOutputStream(Context.getProperty(Config.GIULIETTA_EXPORT_DIR)+File.separator+"output.xls");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		List<LiveSession> sessions = loader.loadAllSessions();
		
		if (sessions.size()==0){
			return;
		}
		String scenarioFile= Context.getProperty(Config.GIULIETTA_SCENARIO_FILE);
		Scenario scenario = null;
		try {
			scenario = loader.loadScenario(new File(scenarioFile),false);
		} catch (InvalidScenarioException e){
			throw new RuntimeException(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//		// create a new workbook
		Workbook workBook = new HSSFWorkbook();
		HSSFCellStyle red = (HSSFCellStyle) workBook.createCellStyle();
		/* We will now specify a background cell color */
		red.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		red.setFillForegroundColor(new HSSFColor.RED().getIndex());
		HSSFCellStyle green = (HSSFCellStyle) workBook.createCellStyle();
		/* We will now specify a background cell color */
		green.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		green.setFillForegroundColor(new HSSFColor.LIGHT_GREEN().getIndex());
		HSSFCellStyle yellow = (HSSFCellStyle) workBook.createCellStyle();
		/* We will now specify a background cell color */
		yellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		yellow.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
		//	        my_style.setFillBackgroundColor(new HSSFColor.RED().getIndex());
		// create a new sheet
		Sheet sheet ;
		Row row;
		Cell cell;
		List<Map<String,Integer>> questionsSummary=new ArrayList<Map<String,Integer>>();

		for (int i =0;i<sessions.get(0).getAnswers().size();++i){
			HashMap<String, Integer> hashMap = new HashMap<String,Integer>();
			hashMap.put("error", 0);
			hashMap.put("invalid", 0);
			hashMap.put("invalidplus", 0);
			hashMap.put("errorplus", 0);
			hashMap.put("missing", 0);
			hashMap.put("good", 0);
			questionsSummary.add(hashMap);
		}
		for (LiveSession session : sessions){
			System.out.println(session.getPerson());
			int row_index = 1;
			Iterator<Item> a = scenario.getItems().iterator();
			sheet = createSheetForUser(workBook, session.getPerson());
			for (Answer ans : session.getAnswers()){
				row = sheet.createRow(row_index);
				cell=row.createCell(0);
				cell.setCellValue("Question " + row_index);
				List<Integer> vraiesReponses = a.next().getReponses();

				
				for (int col_idx = 1;col_idx<4;++col_idx){
					cell=row.createCell(col_idx);
					if (ans.getAnswers().contains(col_idx)){
						cell.setCellValue("X");
						if (!vraiesReponses.contains(col_idx)){
							cell.setCellStyle(red);
						} else {
							cell.setCellStyle(green);
						}
					} else {
						if (vraiesReponses.contains(col_idx)){
							cell.setCellStyle(yellow);

						}
					}

				}
				
				String q = ClassifierHelper.getAnswerStatus(vraiesReponses, ans.getAnswers());
				questionsSummary.get(row_index-1).put(q,questionsSummary.get(row_index-1).get(q)+1);
					
				
				++row_index;
			}

		}

		sheet= workBook.createSheet("Summary");
		row  = sheet.createRow(0);
		cell = row.createCell(1);
		cell.setCellValue("erreur");
		cell = row.createCell(2);
		cell.setCellValue("invalid");
		cell = row.createCell(3);
		cell.setCellValue("missing");
		cell = row.createCell(4);
		cell.setCellValue("good");
		cell = row.createCell(5);
		cell.setCellValue("errorplus");
		cell = row.createCell(6);
		cell.setCellValue("invalidplus");

		//create the summary sheet
		int i=1;
		for (Map<String,Integer> item : questionsSummary){
			row = sheet.createRow(i);
			cell = row.createCell(1);
			cell.setCellValue(item.get("error"));
			cell = row.createCell(2);
			cell.setCellValue(item.get("invalid"));
			cell = row.createCell(3);
			cell.setCellValue(item.get("missing"));
			cell = row.createCell(4);
			cell.setCellValue(item.get("good"));
			cell = row.createCell(5);
			cell.setCellValue(item.get("errorplus"));
			cell = row.createCell(6);
			cell.setCellValue(item.get("invalidplus"));
			++i;
		}

		try {
			workBook.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private Sheet createSheetForUser(Workbook workBook,String name){
		Sheet sheet= workBook.createSheet();
		workBook.setSheetName(workBook.getSheetIndex(sheet), name );
		Row row = sheet.createRow(0);
		Cell cell;
		for (int j = 1;j<4;j++){
			cell=row.createCell(j);
			cell.setCellValue(" Reponse "+j);
		}
		cell=row.createCell(4);
		cell.setCellValue("Erreurs");

		cell=row.createCell(5);
		cell.setCellValue("Mancantes ");

		cell=row.createCell(6);
		cell.setCellValue(" Bonnes ");

		return sheet;

	}



}
