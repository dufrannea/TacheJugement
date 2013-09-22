package giulietta.service;

import giulietta.service.api.Exporter;
import giulietta.service.api.Loader;
import giulietta.service.api.Statistics;
import giulietta.service.impl.ExporterImpl;
import giulietta.service.impl.LoaderImpl;
import giulietta.service.impl.StatisticsImpl;

import org.junit.Test;

public class ReportTest {

	@Test
	public void testLoadSessions(){
		Loader load = new LoaderImpl();
		load.loadAllSessions();
	}
	
	@Test
	public void generateReport(){
		Loader load = new LoaderImpl();
		Statistics stats= new StatisticsImpl();
		Exporter exporter = new ExporterImpl(load,stats);
		exporter.export();
		
	}
	
	@Test
	public void generateFullReport(){
		Loader load = new LoaderImpl();
		Statistics stats= new StatisticsImpl();
		Exporter exporter = new ExporterImpl(load,stats);
		exporter.completeExport();
	}
	
	
}
