package giulietta.service.impl;

import giulietta.config.Config;
import giulietta.model.LiveSession;
import giulietta.model.Question;
import giulietta.service.Context;
import giulietta.service.api.Exporter;
import giulietta.service.api.Loader;
import giulietta.service.api.Statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
			out = new FileOutputStream(Context.getProperty(Config.GIULIETTA_EXPORT_DIR)+File.separator+"output.xls");
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



}
